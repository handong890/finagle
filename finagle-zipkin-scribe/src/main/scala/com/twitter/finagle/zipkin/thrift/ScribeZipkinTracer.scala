package com.twitter.finagle.zipkin.thrift

import com.twitter.finagle.stats.{DefaultStatsReceiver, NullStatsReceiver, StatsReceiver}
import com.twitter.finagle.tracing.Tracer
import com.twitter.finagle.zipkin.{core, host => Host}

object ZipkinTracer {

  lazy val default: Tracer = mk()

  /**
   * @param scribeHost Host to send trace data to
   * @param scribePort Port to send trace data to
   * @param statsReceiver Where to log information about tracing success/failures
   * @param sampleRate How much data to collect. Default sample rate 0.1%. Max is 1, min 0.
   */
  @deprecated("Use mk() instead", "6.1.0")
  def apply(
    scribeHost: String = Host().getHostName,
    scribePort: Int = Host().getPort,
    statsReceiver: StatsReceiver = NullStatsReceiver,
    sampleRate: Float = core.Sampler.DefaultSampleRate
  ): Tracer = mk(scribeHost, scribePort, statsReceiver, sampleRate)

  /**
   * @param host Host to send trace data to
   * @param port Port to send trace data to
   * @param statsReceiver Where to log information about tracing success/failures
   * @param sampleRate How much data to collect. Default sample rate 0.1%. Max is 1, min 0.
   */
  def mk(
    host: String = Host().getHostName,
    port: Int = Host().getPort,
    statsReceiver: StatsReceiver = NullStatsReceiver,
    sampleRate: Float = core.Sampler.DefaultSampleRate
  ): Tracer =
    new ZipkinTracer(
      ScribeRawZipkinTracer(scribeHost = host, scribePort = port, statsReceiver = statsReceiver),
      sampleRate
    )

  /**
   * Util method since named parameters can't be called from Java
   * @param sr stats receiver to send successes/failures to
   */
  @deprecated("Use mk() instead", "6.1.0")
  def apply(sr: StatsReceiver): Tracer =
    mk(Host().getHostName, Host().getPort, sr, core.Sampler.DefaultSampleRate)

  /**
   * Util method since named parameters can't be called from Java
   * @param statsReceiver stats receiver to send successes/failures to
   */
  def mk(statsReceiver: StatsReceiver): Tracer =
    mk(Host().getHostName, Host().getPort, statsReceiver, core.Sampler.DefaultSampleRate)
}

/**
 * Receives the Finagle generated traces and sends a sample of them off to Zipkin via Scribe
 */
class ScribeZipkinTracer(tracer: ScribeRawZipkinTracer, sampler: core.Sampler)
    extends core.SamplingTracer(tracer, sampler) {

  /**
   * Default constructor for the service loader
   */
  private[finagle] def this() =
    this(
      ScribeRawZipkinTracer(
        scribeHost = Host().getHostName,
        scribePort = Host().getPort,
        statsReceiver = DefaultStatsReceiver.scope("zipkin-scribe")
      ),
      DefaultSampler
    )
}

/**
 * Receives the Finagle generated traces and sends a sample of them off to Zipkin via Scribe
 * @param tracer underlying tracer
 * @param sampleRate ratio of requests to trace
 */
class ZipkinTracer(tracer: ScribeRawZipkinTracer, sampleRate: Float)
    extends core.SamplingTracer(tracer, sampleRate) {

  def this(tracer: ScribeRawZipkinTracer, sampler: core.Sampler) =
    this(tracer, sampler.sampleRate)

  def this(tracer: ScribeRawZipkinTracer) =
    this(tracer, DefaultSampler)

}
