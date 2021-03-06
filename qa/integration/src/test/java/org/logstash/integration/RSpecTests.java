package org.logstash.integration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Assert;
import org.junit.Test;
import org.logstash.RubyUtil;
import org.logstash.Rubyfier;

/**
 * JUnit Wrapper for RSpec Integration Tests.
 */
public final class RSpecTests {

    @Test
    public void rspecTests() throws Exception {
        RubyUtil.RUBY.getGlobalVariables().set(
            "$JUNIT_ARGV", Rubyfier.deep(RubyUtil.RUBY, Arrays.asList(
                "-fd", "--pattern", System.getProperty("org.logstash.integration.specs", "specs/**/*_spec.rb")
            ))
        );
        final Path rspec = Paths.get("rspec.rb");
        final IRubyObject result = RubyUtil.RUBY.executeScript(
            new String(Files.readAllBytes(rspec), StandardCharsets.UTF_8),
            rspec.toFile().getAbsolutePath()
        );
        if (!result.toJava(Long.class).equals(0L)) {
            Assert.fail("RSpec test suit saw at least one failure.");
        }
    }
}
