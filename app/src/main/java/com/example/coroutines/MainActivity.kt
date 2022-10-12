package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
class MainActivity : AppCompatActivity() {
    private lateinit var tvLoading: TextView
    private lateinit var btnGo: Button
    private val parentJob = Job()

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLoading = findViewById(R.id.tvLoading)
        btnGo = findViewById(R.id.btnGoSecondActivity)

/*
        GlobalScope.launch {
            val time = measureTimedValue {
                val data1 =  getData1()
                val data2 =  getData2()

                println("Data 1 : ${data1}")
                println("Data 2 : ${data2}")
            }
            println("Data Time Consumed : ${time.duration.inWholeSeconds}")
        }
    }

 */

/*
        GlobalScope.launch(newSingleThreadContext("Loading Thread")) {
            val tv = getData1()
            println("Main -> Current Thread : ${Thread.currentThread().name}")
            withContext(Dispatchers.Main) {
                tvLoading.text = tv
                println("Main -> Current Thread to Update UI : ${Thread.currentThread().name}")
            }
            println("Main -> Current Thread : ${Thread.currentThread().name}")
        }

 */

/*
        GlobalScope.launch(parentJob) {
            val child1: Job?
            val child2: Job?
            val time = measureTimedValue {

                child1 = async { getData1() }
                child1.join()
                println("Main --> Child 1 Finished")

                child2 = async { getData2() }
                child2.join()
                println("Main --> Child 2 Finished")
            }
            child2?.join()
            println("Main --> Time Required : ${time.duration.inWholeSeconds}")
        }

 */

        /*
            val job1 = GlobalScope.launch() {
                withTimeout(4000L) {
                    repeat(10) {
                        println("Main --> Get data from API")
                        delay(1000L)
                    }
                }
                println("Main --> Update UI")
            }
             */

/*
        // Launch Way :
        GlobalScope.launch(Dispatchers.IO) {
            var data1: String? = null
            var data2: String? = null
            val time = measureTimedValue {
                val job1 = launch { data1 = getData1() }
                val job2 = launch { data2 = getData2() }
                // Wait to these two Coroutines to end their execution.
                joinAll(job1, job2)
                println("Main --> $data1 + $data2")

            }
            println("Main --> Time Consumed : ${time.duration.inWholeSeconds}")
            withContext(Dispatchers.Main) {
                tvLoading.text = "$data1 , $data2"
            }
        }

        // Async/await Way :
        GlobalScope.launch {
            val time = measureTimeMillis {
                val data1 = async { getData1() }
                val data2 = async { getData2() }
                withContext(Dispatchers.Main){
                    tvLoading.text = "${data1.await()} + ${data2.await()}"
                }
            }
            println("Main --> Time : $time")
        }

 */

        /*
        // TODO : using lifeCycleScope
        btnGo.setOnClickListener {
            lifecycleScope.launch {
                while (true) {
                    delay(1000L)
                    println("Main : Still Playing ...")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@MainActivity, SecondActivity::class.java).also {
                    startActivity(it)
                }
                finish()
                println("Main : Start New Activity ...")

            }
        }

         */

        /*
        // Exception Handler ..
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("Exception Message : ${throwable.message}")
        }
        GlobalScope.launch(handler) {
            launch {
                delay(2000)
                throw Exception("Cancel")
            }
        }

         */

        /*
        // TODO : Coroutine Scope : if there is an Exception that will cancel all the Coroutines in the same Scope
        // TODO : SuperVisorScope : all coroutines work independently
         val handler = CoroutineExceptionHandler { _, t ->
            println("Catch : ${t.message}")
        }
        val coroutineContext = Dispatchers.IO + handler

        CoroutineScope(Dispatchers.IO + handler).launch {
//            supervisorScope {
                launch {
                    delay(2000)
                    throw Exception("Coroutine 1 Stopped")
                }

                launch {
                    delay(3000)
                    println("Coroutine 2 still Working")
                }
//            }

        }

         */

        /*
        GlobalScope.launch {
            val job = launch {
                try {
                    delay(5000L)
                }catch (e: Exception){
                    if (e is CancellationException){
                        println("The Cor. Stopped !!!")
                        throw e
                    }
                    println("Catch Cor. : ${e.message}")
                }
                repeat(5){
                    println("Cor. still launched")
                }
            }
            delay(2000L)
            job.cancel()
        }

 */


        /*
        // TODO : Channel is One Way of exchanging data between coroutines.
        // TODO : the disadvantages of Channel is it's sending HotStream
        // TODO : the producer coroutine send the data however there is any consumer or not !!
        val list = listOf("A","B","C","D")
        val channel = Channel<String>()
        GlobalScope.launch {
            launch {
                for (char in list) {
                    channel.send(char)
                    println("Channel Send : $char")
                    delay(1000L)
                }
            }

            launch {
                for (char in channel){
                    println("Channel Receive $char")
                }
            }
        }

 */



        /*
        // TODO : Flow instead of Channel.
        GlobalScope.launch {
            flow {
                for (num in 1..20) {
                    // TODO : this function doesn't apply if there is not collect{} block "Consumer"
                    // TODO : Cold Stream
                    emit(num)
                    println("Flow Send : $num")
                    delay(1000)
                }
            }.filter { num -> num % 2 == 0 }
                // Buffer : make the producer and collector in two separate coroutines to work in less time .
                .buffer().collect {
                    println("Flow Receive : $it")
                    delay(2000)
                }
        }

         */


    }

    private suspend fun getData1(): String {
        delay(4000L)
        return "Data One"
    }

    private suspend fun getData2(): String {
        delay(2000L)
        return "Data Two"
    }

    override fun onStop() {
        super.onStop()
        parentJob.cancel()
    }
}