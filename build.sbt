import com.lukaj9.sbtbuf.models._
import com.lukaj9.sbtbuf.platform._

name := "sbt buf example"

scalaVersion := "2.13.14"

enablePlugins(BufPlugin)

ThisBuild / scalacOptions ++= Seq("-Xfatal-warnings", "-Xlint")

bufPath := baseDirectory.value.toPath().resolve("src/main/protobuf")

externalGeneration := List(
  ExternalGeneration(
    location = "https://github.com/grpc/grpc.git#tag=v1.65.0",
    path = Some("src/proto/grpc/health/v1/health.proto")
  )
)

lazy val fs2Grpc = "https://repo1.maven.org/maven2/org/typelevel/protoc-gen-fs2-grpc/2.7.16/protoc-gen-fs2-grpc-2.7.16"
protocPlugins := Seq(
    com.lukaj9.sbtbuf.platform.ProtocPlugin(
        "protoc-gen-fs2-grpc",
        detectedSystem.value.os match {
            case Windows => new URI(s"$fs2Grpc-windows.bat")
            case Linux | Osx => new URI(s"$fs2Grpc-unix.sh")
            case e => throw new IllegalArgumentException(s"$e is not a supported os for this plugin")
        }
    )
)

bufBreakingCommand := BreakingCommand(
  agaisnt = AgainstBreaking(bufPath.value.toAbsolutePath.toString())
)

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)
