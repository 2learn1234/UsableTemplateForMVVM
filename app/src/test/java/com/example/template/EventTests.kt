package com.example.template

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.template.data.repository.EventRepository
import com.example.template.data.webdata.UrlData
import com.example.template.data.webdata.WebDataApi
import com.example.template.data.webdata.json.EmbeddedEvents
import com.example.template.domain.Event
import com.example.template.ui.list.catalog.CatalogViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class EventTests {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    lateinit var cvm : CatalogViewModel

    @MockK
    lateinit var mockWebDataService : WebDataApi

    @MockK
    lateinit var mockRepository: EventRepository

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun `given a view model with live data when populated with events then results show myEvent and myType` () {
        givenViewModelIsInitializedWithMockData()
        whenEventServicerRefreshInvoked()
        thenResultsShouldContainMyEventAndMyType()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val events = mutableListOf<Event>()
        events.add(Event("0", "myEvent", "EasternEvent", ""))
        val redOak = Event("1", "myEvent1", "EasternEvent1", "")
        events.add(redOak)
        events.add(Event("2", "myEvent2", "EasternEvent2", ""))

        val response=mockk<Response<EmbeddedEvents>>()
        coEvery { mockWebDataService.getCatalog(UrlData.APIKEY) }  returns mockk<Response<EmbeddedEvents>>()
        cvm = CatalogViewModel(repository=mockRepository)
    }

    private fun whenEventServicerRefreshInvoked() {
        cvm.refresh()
    }

    private fun thenResultsShouldContainMyEventAndMyType() {
        var allEvents : List<Event>? = ArrayList<Event>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Event>> {
            override fun onChanged(receivedEvents:List<Event>) {
                allEvents = receivedEvents
                latch.countDown()
                cvm.liveEventList.removeObserver(this)
            }
        }
        cvm.liveEventList.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allEvents)
        assertTrue(allEvents!!.isNotEmpty())
        var containsCercisCanadensis = false
        allEvents!!.forEach {
            if (it.name.equals(("myName")) && it.type.equals("myType")) {
                containsCercisCanadensis = true
            }
        }
        assertTrue(containsCercisCanadensis)
    }

}