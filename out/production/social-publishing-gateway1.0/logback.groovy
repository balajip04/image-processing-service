import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import java.nio.charset.Charset
import org.springframework.boot.ApplicationPid
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter
import org.springframework.boot.logging.logback.LevelRemappingAppender
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN

conversionRule("clr", ColorConverter)
conversionRule("wex", WhitespaceThrowableProxyConverter)
conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter)

def pid = new ApplicationPid().toString()

def CONSOLE_LOG_PATTERN = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr('+pid+'){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx'
def FILE_LOG_PATTERN = '%d{yyyy-MM-dd HH:mm:ss.SSS} %5p '+pid+' --- [%t] %-40.40logger{39} : %m%n%wEx'

def LOG_FILE = 'social-publishing-gateway1.0.log'

//hostName will be container id if running inside a docker container
def hostName = System.getenv("HOSTNAME")

if (hostName != null) {
	LOG_FILE = 'social-publishing-gateway1.0.' + hostName + '.log'
}

def logPath = '/apps/docker/logs/splunkme/social-publishing-gateway1.0'

def path = new File(logPath)

if (path.exists()) {
	LOG_FILE = logPath + '/' + LOG_FILE
} else if (path.mkdirs()) {
	LOG_FILE = logPath + '/' + LOG_FILE
} else {
	LOG_FILE = System.getProperty("java.io.tmpdir") + '/' + LOG_FILE
}

 
appender("DEBUG_LEVEL_REMAPPER", LevelRemappingAppender) { destinationLogger = "org.springframework.boot" }

appender("CONSOLE", ConsoleAppender) {
	encoder(PatternLayoutEncoder) {
		pattern = "${CONSOLE_LOG_PATTERN}"
		charset = Charset.forName("utf8")
	}
}

appender("FILE", RollingFileAppender) {
	encoder(PatternLayoutEncoder) { pattern = "${FILE_LOG_PATTERN}" }
	file = "${LOG_FILE}"
	rollingPolicy(FixedWindowRollingPolicy) { fileNamePattern = "${LOG_FILE}.%i" }
	triggeringPolicy(SizeBasedTriggeringPolicy) { maxFileSize = "10MB" }
}

root(INFO, ["CONSOLE", "FILE"])

logger("com.cars.social", ERROR, ["CONSOLE"], false)
logger("org.apache.catalina.startup.DigesterFactory", ERROR)
logger("org.apache.catalina.util.LifecycleBase", ERROR)
logger("org.apache.coyote.http11.Http11NioProtocol", WARN)
logger("org.apache.sshd.common.util.SecurityUtils", WARN)
logger("org.apache.tomcat.util.net.NioSelectorPool", WARN)
logger("org.crsh.plugin", WARN)
logger("org.crsh.ssh", WARN)
logger("org.eclipse.jetty.util.component.AbstractLifeCycle", ERROR)
logger("org.hibernate.validator.internal.util.Version", WARN)
logger("org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration", WARN)
logger('org.springframework.boot.actuate.endpoint.jmx', WARN, ["DEBUG_LEVEL_REMAPPER"], false)
logger('org.thymeleaf', WARN, ["DEBUG_LEVEL_REMAPPER"], false)
