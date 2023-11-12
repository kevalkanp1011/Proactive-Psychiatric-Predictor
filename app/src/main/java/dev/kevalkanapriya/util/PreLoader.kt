package dev.kevalkanapriya.util

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import kotlin.system.exitProcess

class Preloader {


    var productInfo = mutableListOf<ProductInfo>()
    var future: FutureTask<MutableList<ProductInfo>>? = null

    fun get(): List<ProductInfo> {
        try {
            //get is blocking
            productInfo = future!!.get()
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: ExecutionException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return productInfo
    }

    fun cancel(): Boolean {
        return future!!.cancel(true)
    }

    fun isDone(): Boolean {
        return future!!.isDone
    }


    fun start() {
        println("The task is being submitted now...")

        future = executor.submit(Callable {
            loadProductInfo()
        }) as FutureTask<MutableList<ProductInfo>>
    }

    //long running task


    private fun loadProductInfo(): List<ProductInfo> {
        println(Thread.currentThread().name)
        try {
            Thread.sleep(10000)
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


        //As if this data we have got from
        //the database
        for (i in 0..999) {
            val productI = ProductInfo(i.toString(), i)
            println("new Product: ${productI.productName} && ${productI.productPrice}")
            productInfo.add(productI)
        }
        return productInfo
    }

    companion object {
        val executor = Executors.newFixedThreadPool(1)
    }
}

data class ProductInfo(
    val productName: String,
    val productPrice: Int
)

fun main() {
    var listOfProductInfo: List<ProductInfo>? = null

    println(Thread.currentThread().name)

    val preloader = Preloader()
    preloader.start()

    try {
        println("Thread Sleep Before 50000 called")
        Thread.sleep(50000)
        println("Thread Sleep 50000 called")
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    var count = 0
    while (!preloader.isDone()){
        println("Task is yet to be completed...");
        count++

        try {
            Thread.sleep(1000);
        } catch (e: InterruptedException) {
            e.printStackTrace();
            Preloader.executor.shutdown();
            exitProcess(0)
        }
        if (count == 100) {
            preloader.cancel();
            println("The task has been cancelled...");
            Preloader.executor.shutdown();
            exitProcess(0)
        }
    }

    if(!preloader.cancel() && preloader.isDone()){
        listOfProductInfo = preloader.get()
    }


    println("productInfo Name ${listOfProductInfo?.get(0)?.productName}")
    println("productInfo Price ${listOfProductInfo?.get(0)?.productPrice}")

    Preloader.executor.shutdown();
}