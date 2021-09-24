import sbt._

object Dependencies {

    object Version {
        val scalaLogging            = "3.7.2"
        val logstashLogbackEncoder  = "4.11"
        val akkaHttpVersion         = "10.1.11"
        val akkaVersion             = "2.5.31"
        val logback                 = "1.2.3"
        val akkaCors                = "0.2.2"
        val akkaJson                = "1.19.0"
        val json4s                  = "3.5.3"
        val json4sExt               = "3.5.3"
        val akkaGuice               = "3.2.0"
        val akkaManagement          = "1.0.7"
        val playJson                = "2.9.0"
    }

    val resolvers: Vector[Resolver] = Resolver.combineDefaultResolvers(Vector(
        "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
        Resolver.url(
            "Java.net Maven2 Repository",
            new java.net.URL("http://download.java.net/maven/2/")
        )(Resolver.ivyStylePatterns)
    ), mavenCentral = true)


    val libraryDependencies = Seq(
        "com.typesafe.scala-logging"        %% "scala-logging"                  % Version.scalaLogging,
        "ch.qos.logback"                    %  "logback-core"                   % Version.logback,
        "ch.qos.logback"                    %  "logback-classic"                % Version.logback,
        "ch.qos.logback"                    %  "logback-access"                 % Version.logback,
        "net.logstash.logback"              %  "logstash-logback-encoder"       % Version.logstashLogbackEncoder,
        "com.typesafe.akka"                 %% "akka-http-xml"                  % Version.akkaHttpVersion,
        "com.typesafe.akka"                 %% "akka-stream"                    % Version.akkaVersion,
        "com.typesafe.akka"                 %% "akka-slf4j"                     % Version.akkaVersion,
        "com.typesafe.akka"                 %% "akka-actor-typed"               % Version.akkaVersion,
        "com.typesafe.akka"                 %% "akka-cluster-sharding-typed"    % Version.akkaVersion,
        "com.typesafe.akka"                 %% "akka-discovery"                 % Version.akkaVersion,
        "com.lightbend.akka.management"     %% "akka-management"                % Version.akkaManagement,
        "com.lightbend.akka.management"     %% "akka-management-cluster-http"   % Version.akkaManagement,
        "com.sandinh"                       %% "akka-guice"                     % Version.akkaGuice,
        "com.typesafe.play"                 %% "play-json"                      % Version.playJson
    )
}
