package demo.server

import io.micrometer.common.KeyValue
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.stream.StreamSupport


@Component
class MyHandler : ObservationHandler<Observation.Context> {

    private val log = KotlinLogging.logger {}

    override fun onStart(context: Observation.Context) {
        log.info("Before running the observation for context [{}], userType [{}]", context.name, getUserTypeFromContext(context))
    }

    override fun onStop(context: Observation.Context) {
        log.info("After running the observation for context [{}], userType [{}]", context.name, getUserTypeFromContext(context))
    }

    override fun supportsContext(context: Observation.Context): Boolean {
        return true
    }

    private fun getUserTypeFromContext(context: Observation.Context): String {
        return StreamSupport.stream(context.lowCardinalityKeyValues.spliterator(), false)
                .filter { keyValue: KeyValue -> "userType" == keyValue.key }
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN")
    }
}