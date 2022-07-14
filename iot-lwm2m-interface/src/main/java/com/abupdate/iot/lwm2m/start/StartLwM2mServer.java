package com.abupdate.iot.lwm2m.start;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.lwm2m.core.model.ObjectLoader;
import org.eclipse.lwm2m.core.model.ObjectModel;
import org.eclipse.lwm2m.core.node.codec.DefaultLwM2mNodeDecoder;
import org.eclipse.lwm2m.core.node.codec.DefaultLwM2mNodeEncoder;
import org.eclipse.lwm2m.server.impl.FileSecurityStore;
import org.eclipse.lwm2m.server.model.LwM2mModelProvider;
import org.eclipse.lwm2m.server.model.StaticModelProvider;
import org.eclipse.lwm2m.server.security.EditableSecurityStore;
import org.eclipse.lwm2m.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.redis.RedisRegistrationStore;
import com.abupdate.iot.lwm2m.redis.RedisSecurityStore;
import com.abupdate.iot.lwm2m.server.LwM2mServer;
import com.abupdate.iot.lwm2m.server.LeshanServerBuilder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 **/
public class StartLwM2mServer {
    private static final Logger logger = LoggerFactory.getLogger(StartLwM2mServer.class);

    private final static String DEFAULT_KEYSTORE_TYPE = KeyStore.getDefaultType();

    private final static String DEFAULT_KEYSTORE_ALIAS = "Abupdate";

    public static void startLwM2mServer(String redis) {
        // Define options for command line tools
        Options options = new Options();
        options.addOption("h", "help", false, "Display help information.");
        options.addOption("lh", "coaphost", true, "Set the local CoAP address.\n  Default: any local address.");
        options.addOption("lp", "coapport", true, String.format("Set the local CoAP port.\n  Default: %d.", LwM2m.DEFAULT_COAP_PORT));
        options.addOption("slh", "coapshost", true, "Set the secure local CoAP address.\nDefault: any local address.");
        options.addOption("slp", "coapsport", true, String.format("Set the secure local CoAP port.\nDefault: %d.", LwM2m.DEFAULT_COAP_SECURE_PORT));
        options.addOption("ks", "keystore", true, "Set the key store file. If set, X.509 mode is enabled, otherwise built-in RPK credentials are used.");
        options.addOption("ksp", "storepass", true, "Set the key store password.");
        options.addOption("kst", "storetype", true, String.format("Set the key store type.\nDefault: %s.", DEFAULT_KEYSTORE_TYPE));
        options.addOption("ksa", "alias", true, String.format("Set the key store alias to use for server credentials.\nDefault: %s.", DEFAULT_KEYSTORE_ALIAS));
        options.addOption("ksap", "keypass", true, "Set the key store alias password to use.");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);

        // Parse arguments
        CommandLine commandLine;
        try {
            commandLine = new DefaultParser().parse(options, null);
        } catch (ParseException e) {
            logger.info("Parsing failed.  Reason: " + e.getMessage());
            return;
        }

        // Abort if unexpected options
        if (commandLine.getArgs().length > 0) {
            logger.info("Unexpected option or arguments : " + commandLine.getArgList());
            return;
        }

        // get local address Coap
        String localAddress = commandLine.getOptionValue("lh");
        String localPortOption = commandLine.getOptionValue("lp");
        int localPort = LwM2m.DEFAULT_COAP_PORT;
        if (localPortOption != null) {
            localPort = Integer.parseInt(localPortOption);
        }

        // get secure local address Coaps
        String secureLocalAddress = commandLine.getOptionValue("slh");
        String secureLocalPortOption = commandLine.getOptionValue("slp");
        int secureLocalPort = LwM2m.DEFAULT_COAP_SECURE_PORT;
        if (secureLocalPortOption != null) {
            secureLocalPort = Integer.parseInt(secureLocalPortOption);
        }

        // get the Redis hostname:port
        String redisUrl = redis;

        // Get keystore parameters
        String keyStorePath = commandLine.getOptionValue("ks");
        String keyStoreType = commandLine.getOptionValue("kst", KeyStore.getDefaultType());
        String keyStorePass = commandLine.getOptionValue("ksp");
        String keyStoreAlias = commandLine.getOptionValue("ksa");
        String keyStoreAliasPass = commandLine.getOptionValue("ksap");

        try {
            logger.info("coap server start" + localAddress + ":" + localPort);
            createAndStartServer(localAddress, localPort, redisUrl, secureLocalAddress, secureLocalPort, keyStorePath, keyStoreType, keyStorePass, keyStoreAlias, keyStoreAliasPass);
        } catch (Exception e) {
            logger.error("Jetty stopped with unexpected error ...", e);
        }
    }

    public static void createAndStartServer(String localAddress, int localPort, String redisUrl, String secureLocalAddress,
                                            int secureLocalPort, String keyStorePath, String keyStoreType, String keyStorePass, String keyStoreAlias
            , String keyStoreAliasPass) throws Exception {
        //创建构造器
        LeshanServerBuilder builder = new LeshanServerBuilder();
        builder.setLocalAddress(localAddress, localPort);
        builder.setLocalSecureAddress(secureLocalAddress, secureLocalPort);
        builder.setDecoder(new DefaultLwM2mNodeDecoder());
        builder.setEncoder(new DefaultLwM2mNodeEncoder());

        //设置CoAP配置文件
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = LeshanServerBuilder.createDefaultNetworkConfig();
            coapConfig.store(configFile);
        }
        builder.setCoapConfig(coapConfig);

        //设置权限、登录相关信息存储方式
        Pool<Jedis> jedis = null;
        if (redisUrl != null) {
            jedis = new JedisPool(new URI(redisUrl));
        }
        EditableSecurityStore securityStore;
        if (jedis == null) {
            //文件存储
            securityStore = new FileSecurityStore();
        } else {
            //redis存储
            securityStore = new RedisSecurityStore(jedis);
            builder.setRegistrationStore(new RedisRegistrationStore(jedis));
        }
        builder.setSecurityStore(securityStore);

        // 设置X.509认证
        PublicKey publicKey = null;
        if (keyStorePath != null) {
            try {
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                try (FileInputStream fis = new FileInputStream(keyStorePath)) {
                    keyStore.load(fis, keyStorePass == null ? null : keyStorePass.toCharArray());
                    List<Certificate> trustedCertificates = new ArrayList<>();
                    for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements(); ) {
                        String alias = aliases.nextElement();
                        if (keyStore.isCertificateEntry(alias)) {
                            trustedCertificates.add(keyStore.getCertificate(alias));
                        } else if (keyStore.isKeyEntry(alias) && alias.equals(keyStoreAlias)) {
                            List<X509Certificate> x509CertificateChain = new ArrayList<>();
                            Certificate[] certificateChain = keyStore.getCertificateChain(alias);
                            if (certificateChain == null || certificateChain.length == 0) {
                                logger.error("Keystore alias must have a non-empty chain of X509Certificates.");
                                System.exit(-1);
                            }

                            for (Certificate certificate : certificateChain) {
                                if (!(certificate instanceof X509Certificate)) {
                                    logger.error("Non-X.509 certificate in alias chain is not supported: {}", certificate);
                                    System.exit(-1);
                                }
                                x509CertificateChain.add((X509Certificate) certificate);
                            }

                            Key key = keyStore.getKey(alias,
                                    keyStoreAliasPass == null ? new char[0] : keyStoreAliasPass.toCharArray());
                            if (!(key instanceof PrivateKey)) {
                                logger.error("Keystore alias must have a PrivateKey entry, was {}", key == null ? null : key.getClass().getName());
                                System.exit(-1);
                            }
                            builder.setPrivateKey((PrivateKey) key);
                            publicKey = keyStore.getCertificate(alias).getPublicKey();
                            builder.setCertificateChain(x509CertificateChain.toArray(new X509Certificate[x509CertificateChain.size()]));
                        }
                    }
                    builder.setTrustedCertificates(trustedCertificates.toArray(new Certificate[trustedCertificates.size()]));
                }
            } catch (KeyStoreException | IOException e) {
                logger.error("Unable to initialize X.509.", e);
                System.exit(-1);
            }
        }
        // 设置RPK认证
        else {
            try {
                // Get point values
                byte[] publicX = Hex.decodeHex("fcc28728c123b155be410fc1c0651da374fc6ebe7f96606e90d927d188894a73".toCharArray());
                byte[] publicY = Hex.decodeHex("d2ffaa73957d76984633fc1cc54d0b763ca0559a9dff9706e9f4557dacc3f52a".toCharArray());
                byte[] privateS = Hex.decodeHex("1dae121ba406802ef07c193c1ee4df91115aabd79c1ed7f4c0ef7ef6a5449400".toCharArray());
                // Get Elliptic Curve Parameter spec for secp256r1
                AlgorithmParameters algoParameters = AlgorithmParameters.getInstance("EC");
                algoParameters.init(new ECGenParameterSpec("secp256r1"));
                ECParameterSpec parameterSpec = algoParameters.getParameterSpec(ECParameterSpec.class);
                // Create key specs
                KeySpec publicKeySpec = new ECPublicKeySpec(new ECPoint(new BigInteger(publicX), new BigInteger(publicY)), parameterSpec);
                KeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(privateS), parameterSpec);
                // Get keys
                publicKey = KeyFactory.getInstance("EC").generatePublic(publicKeySpec);
                PrivateKey privateKey = KeyFactory.getInstance("EC").generatePrivate(privateKeySpec);
                builder.setPublicKey(publicKey);
                builder.setPrivateKey(privateKey);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidParameterSpecException e) {
                logger.error("Unable to initialize RPK.", e);
                System.exit(-1);
            }
        }

        //加载模型
        List<ObjectModel> models = ObjectLoader.loadDefault();
        LwM2mModelProvider modelProvider = new StaticModelProvider(models);
        builder.setObjectModelProvider(modelProvider);

        LwM2mServer server = builder.build();
        //入口，创建启动lwm2m服务端
        server.start();
    }

    public static void main(String[] args) throws URISyntaxException {
        String redisUrl = "redis://127.0.0.1:6379";
        Pool<Jedis> jedis = null;
        if (redisUrl != null) {
            jedis = new JedisPool(new URI(redisUrl));
        }
        System.out.println("1111111");
    }
}
