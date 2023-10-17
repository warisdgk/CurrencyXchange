package mwaris.dev.currencyxchange.model

import mwaris.dev.currencyxchange.data.remote.model.LatestCurrenciesInfo
import mwaris.dev.currencyxchange.data.remote.model.asEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class LatestCurrenciesInfoTest {

    @Test
    fun latestCurrenciesInfoCanBeMappedToEntity() {
        val latestCurrenciesInfo = LatestCurrenciesInfo(
            timestamp = 12432543,
            baseCurrency = "USD",
            rates = mapOf(
                "AED" to 1.33
            ),
        )
        val entity = latestCurrenciesInfo.asEntity()

        assertEquals(12432543, entity.timestamp)
        assertEquals("USD", entity.baseCurrency)
        assertEquals(
            mapOf(
                "AED" to 1.33
            ), entity.rates
        )
    }

}