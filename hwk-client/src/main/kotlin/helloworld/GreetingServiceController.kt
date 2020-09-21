package helloworld

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Controller
class GreetingServiceClient(private val greetingClient : GreeterGrpcKt.GreeterCoroutineStub) {


    @Get("/hello/{name}")
    fun callGreetingService(name: String): String? {
        return runBlocking {
            greetingClient.sayHello(
                    HelloRequest.newBuilder().setName(name).build()
            ).message
        }
    }

    @Get("/hola/{name}")
    fun kallGreetingService(name: String): String? {
        val client = GreeterGrpcKt.GreeterCoroutineStub(
                ManagedChannelBuilder.forTarget("hwk-server:8081").usePlaintext().build()
        )
        return runBlocking {
            client.sayHello(
                    HelloRequest.newBuilder().setName(name).build()
            ).message
        }
    }
}

@Factory
class Clients {
    @Singleton
    fun greetingClient( @GrpcChannel("hwk-server") channel : ManagedChannel ) : GreeterGrpcKt.GreeterCoroutineStub {
        return GreeterGrpcKt.GreeterCoroutineStub(
                channel
        )
    }
}
