package com.biao.config;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;

public class FreemarkConfig {

    private static FreemarkConfig config = new FreemarkConfig();

    private Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    private StringTemplateLoader stringLoader = new StringTemplateLoader();

    private FreemarkConfig() {
        cfg.setTemplateLoader(stringLoader);
        cfg.setNumberFormat("0.##");
    }

    public static FreemarkConfig newInstance() {
        return config;
    }

    public Configuration getConfiguration() {
        return cfg;
    }

    public Configuration setTemplateLoaderConfiguration() {
        cfg.setTemplateLoader(stringLoader);
        return cfg;
    }

    public StringTemplateLoader getStringTemplateLoader() {
        return stringLoader;
    }
}
