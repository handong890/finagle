scala_library(
    sources = rglobs("scala/*.scala", "scala-2.12-/*.scala"),
    compiler_option_sets = {"fatal_warnings"},
    provides = scala_artifact(
        org = "com.twitter",
        name = "finagle-base-http",
        repo = artifactory,
    ),
    dependencies = [
        "3rdparty/jvm/com/google/code/findbugs:jsr305",
        "3rdparty/jvm/io/netty:netty4",
        "3rdparty/jvm/io/netty:netty4-codec-http",
        "finagle/finagle-base-http/src/main/resources",
        "finagle/finagle-core",
        "finagle/finagle-toggle/src/main/scala",
        "util/util-app",
        "util/util-codec",
        "util/util-core",
        "util/util-stats",
    ],
    exports = [
        "util/util-core",
    ],
)
