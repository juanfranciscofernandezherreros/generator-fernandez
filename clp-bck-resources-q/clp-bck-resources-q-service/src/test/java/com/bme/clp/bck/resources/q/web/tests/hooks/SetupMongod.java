package com.bme.clp.bck.resources.q.web.tests.hooks;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.bme.clp.bck.resources.q.infrastructure.spring.config.MongoConfigurer;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongoImportExecutable;
import de.flapdoodle.embed.mongo.MongoImportProcess;
import de.flapdoodle.embed.mongo.MongoImportStarter;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.Defaults;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoImportConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.process.config.RuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;

public class SetupMongod implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    private static final String PATH_OBJECT = "json/objects/collection/collection.json";

    private MongodExecutable mongodExecutable;

    private void loadCollection() throws IOException {
        File jsonFile = new File(Thread.currentThread().getContextClassLoader().getResource(PATH_OBJECT).getFile());
        MongoImportConfig mongoImportConfig = MongoImportConfig.builder()
                .version(MongoConfigurer.VERSION)
                .net(new Net(MongoConfigurer.PORT, Network.localhostIsIPv6()))
                .databaseName(MongoConfigurer.DATABASE)
                .collectionName(MongoConfigurer.COLLECTION)
                .isUpsertDocuments(true)
                .isDropCollection(true)
                .isJsonArray(true)
                .importFile(jsonFile.getAbsolutePath())
        .build();

        MongoImportExecutable mongoImportExecutable = MongoImportStarter.
               getDefaultInstance().prepare(mongoImportConfig);

        MongoImportProcess mongoImportProcess = mongoImportExecutable.start();
        mongoImportProcess.stop();
    }

    private MongodExecutable getMongoExecutable() throws UnknownHostException {
        Command command = Command.MongoD;
        RuntimeConfig runtimeConfig = Defaults.runtimeConfigFor(command)
                .artifactStore(Defaults.extractedArtifactStoreFor(command)
                    .withDownloadConfig(Defaults.downloadConfigFor(command)
                        .downloadPath((__) -> MongoConfigurer.BINARY_URI)
                        .build()))
                .build();
                ImmutableMongodConfig mongodConfig = MongodConfig.builder().version(MongoConfigurer.VERSION)
                    .net(new Net(MongoConfigurer.HOST, MongoConfigurer.PORT, Network.localhostIsIPv6())).build();
                MongodStarter starter = MongodStarter.getInstance(runtimeConfig);
                return starter.prepare(mongodConfig);
            }

    @Override
    public void beforeAll(final ExtensionContext context) throws IOException {
        if (!started) {
            started = true;
            mongodExecutable = getMongoExecutable();
            mongodExecutable.start();
            this.loadCollection();
            context.getRoot().getStore(GLOBAL).put("Hook" + UUID.randomUUID(), this);
        }
    }

    @Override
    public void close() {
        mongodExecutable.stop();
    }
}