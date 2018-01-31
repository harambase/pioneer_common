//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
// de.nixosoft.jlr
//

package com.harambase.pioneer.common.support.document.jlr;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.charset.Charset;

public class JLRConverter {
    private String errorMessage;
    private String defaultCharsetName;
    private VelocityContext contextData;
    private VelocityEngine velocityEngine;

    public JLRConverter(File var1) {
        this(var1, "org.apache.velocity.runtime.log.NullLogChute");
    }

    public JLRConverter(File var1, String var2) {
        this.errorMessage = "No errors occurred!";
        this.defaultCharsetName = Charset.defaultCharset().name();
        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setProperty("file.resource.loader.path", var1.getAbsolutePath());
        this.velocityEngine.setProperty("runtime.log.logsystem.class", var2);
        this.velocityEngine.init();
        this.contextData = new VelocityContext();
    }

    public void replace(String key, Object var2) {
        this.contextData.put(key, var2);
    }

    public void clear() {
        this.contextData = new VelocityContext();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean parse(String var1, File var2) throws IOException {
        try {
            return this.parse(var1, var2, this.defaultCharsetName);
        } catch (UnsupportedEncodingException var4) {
            this.errorMessage = var4.toString();
            return false;
        }
    }

    public boolean parse(String var1, File var2, String var3) throws IOException {
        if (var2.getParentFile() != null && !var2.getParentFile().isDirectory() && !var2.getParentFile().mkdirs()) {
            this.errorMessage = "Could not createPerson directory: " + var2.getParentFile().getAbsolutePath();
            return false;
        } else {
            BufferedReader var4 = new BufferedReader(new StringReader(var1));
            BufferedWriter var5 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), var3));
            if (!this.velocityEngine.evaluate(this.contextData, var5, "myTemplate", var4)) {
                this.errorMessage = "Errors occurred and logged to velocity log";
                var5.flush();
                var5.close();
                return false;
            } else {
                var5.flush();
                var5.close();
                return true;
            }
        }
    }

    public boolean parse(File var1, File var2) throws IOException {
        try {
            return this.parse(var1, var2, this.defaultCharsetName);
        } catch (UnsupportedEncodingException var4) {
            this.errorMessage = var4.toString();
            return false;
        }
    }

    public boolean parse(File var1, File var2, String var3) throws IOException {
        if (var2.getParentFile() != null && !var2.getParentFile().isDirectory() && !var2.getParentFile().mkdirs()) {
            this.errorMessage = "Could not createPerson directory: " + var2.getParentFile().getAbsolutePath();
            return false;
        } else {
            BufferedWriter var4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), var3));
            if (!this.velocityEngine.mergeTemplate(var1.getName(), var3, this.contextData, var4)) {
                this.errorMessage = "Errors occurred and logged to velocity log";
                var4.flush();
                var4.close();
                return false;
            } else {
                var4.flush();
                var4.close();
                return true;
            }
        }
    }
}
