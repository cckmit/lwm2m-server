package com.abupdate.iot.lwm2m;

import java.io.File;
import java.net.InetSocketAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfig.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.server.AbupdateServer;
import com.abupdate.iot.lwm2m.util.LwM2m;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月2日
 **/

@SpringBootApplication
public class StartLwM2mServer {
    private static final Logger logger = LoggerFactory.getLogger(StartLwM2mServer.class);

    public static void main(String[] args) {


        SpringApplication.run(StartLwM2mServer.class);
//        File logbackFile = new File("logback.xml");
//        if (logbackFile.exists()) {
//            logger.info("loading logback Config file.............................");
//            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//            JoranConfigurator configurator = new JoranConfigurator();
//            configurator.setContext(lc);
//            lc.reset();
//            try {
//                configurator.doConfigure(logbackFile);
//            } catch (JoranException e) {
//                e.printStackTrace(System.err);
//                logger.info("loading logback.xml Config file............................. Error ................");
//            }
//        }

        logger.info("Start CoAP Server.............................");

        Options options = new Options();
        options.addOption("lh", "coaphost", true, "Set the local CoAP address.\n  Default: any local address.");
        options.addOption("slh", "coapshost", true, "Set the secure local CoAP address.\nDefault: any local address.");

        logger.info("parse arguments begin....");
        // Parse arguments
        CommandLine cl;
        try {
            cl = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            logger.info("Parsing failed.  Reason: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        //IP Port
        InetSocketAddress localAddress;
        String localAddressName = cl.getOptionValue("lh");
        if (localAddressName == null) {

            localAddress = new InetSocketAddress(LwM2m.DEFAULT_COAP_PORT);

        } else {
            localAddress = new InetSocketAddress(localAddressName, LwM2m.DEFAULT_COAP_PORT);
        }

        //Coap config
        NetworkConfig coapConfig;
        File configFile = new File(NetworkConfig.DEFAULT_FILE_NAME);
        if (configFile.isFile()) {
            logger.info("loading CoAP Config file.............................");
            coapConfig = new NetworkConfig();
            coapConfig.load(configFile);
        } else {
            coapConfig = new NetworkConfig();
            coapConfig.set(Keys.MID_TRACKER, "NULL");
            coapConfig.store(configFile);
        }

        // create endpoints
        CoapEndpoint securedEndpoint = new CoapEndpoint(localAddress, coapConfig);

        logger.info("create endpoints success");
        AbupdateServer server = new AbupdateServer(coapConfig, securedEndpoint);
        server.start();
    }
}

