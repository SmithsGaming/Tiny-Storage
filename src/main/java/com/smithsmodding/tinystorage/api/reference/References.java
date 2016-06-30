package com.smithsmodding.tinystorage.api.reference;

/**
 * Created by Tim on 19/06/2016.
 */
public class References {

    public static final String MOD_ID = "TinyStorage";
    public static final String MOD_NAME = "Tiny Storage";
    public static final String VERSION = "@VERSION@";
    public static final String API_VERSION = "@APIVERSION@";
    public static final String SERVER_PROXY_CLASS = "com.smithsmodding.tinystorage.common.proxy.ServerProxy";
    public static final String CLIENT_PROXY_CLASS = "com.smithsmodding.tinystorage.client.proxy.ClientProxy";

    public static final String TE = "TinyStorage.TE.General";

    public static class Modules {

        public static class Limits {
            public static final int SMALL_SIZE = 3;
            public static final int MEDIUM_SIZE = 6;
            public static final int LARGE_SIZE = 9;
        }

        public static class Storage {
            public static final int SMALL_SIZE = 7;
            public static final int MEDIUM_SIZE = 14;
            public static final int LARGE_SIZE = 21;
        }

        public static class Filter {
            public static final int SMALL_SIZE = 3;
            public static final int MEDIUM_SIZE = 6;
            public static final int LARGE_SIZE = 9;
        }

        public class ModuleNames {
            public static final String CORE = "tinystorage.core";
            public static final String SMALL_STORAGE_MODULE = "tinystorage.storage.small";
            public static final String MEDIUM_STORAGE_MODULE = "tinystorage.storage.medium";
            public static final String LARGE_STORAGE_MODULE = "tinystorage.storage.large";
            public static final String SMALL_FILTER_MODULE = "tinystorage.filter.small";
            public static final String MEDIUM_FILTER_MODULE = "tinystorage.filter.medium";
            public static final String LARGE_FILTER_MODULE = "tinystorage.filter.large";
        }
    }

    public class Items {
        public static final String ITEMMODULE = "tinystorage.items.module";
    }

    public class Blocks {
        public static final String BLOCKCHESTBASE = "tinystorage.blocks.base";

        public class Properties {
            public static final String MODULES = "tinystorage.blocks.propeties.modules";
        }
    }

    public class NBT {
        public class Blocks {
            public class Chests {
                public static final String INSTALLEDMODULES = "Modules";
                public static final String MODULEID = "ModuleID";
                public static final String MODULEDATA = "ModuleData";
            }
        }

        public class Inventory {
            public static final String SLOT = "Slot";
            public static final String INVENTORY = "Inventory";
        }
    }
}
