package adapters.network

import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

fun makeRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(makeHttpClient())
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(produceGson()))
    .build()

private fun produceGson() = Gson().newBuilder()
    .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
    .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
    .serializeNulls().create()

private fun makeHttpClient() = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(loggingInterceptor())
    .addInterceptor(headersInterceptor())
    .build()

fun headersInterceptor() = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "en")
            .addHeader("Content-Type", "application/json")
            .build()
    )
}

fun loggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
    return httpLoggingInterceptor
}

class DateTimeDeserializer : JsonDeserializer<DateTime?>, JsonSerializer<DateTime?> {
    @Throws(JsonParseException::class)
    override fun deserialize(je: JsonElement, type: Type?, jdc: JsonDeserializationContext?): DateTime? {
        return if (je.asString.isEmpty()) null else DATE_TIME_FORMATTER.parseDateTime(je.asString)
    }

    override fun serialize(src: DateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(if (src == null) "" else DATE_TIME_FORMATTER.print(src))
    }

    companion object {
        val DATE_TIME_FORMATTER: DateTimeFormatter = ISODateTimeFormat.dateTime()
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate?>, JsonSerializer<LocalDate?> {
    @Throws(JsonParseException::class)
    override fun deserialize(je: JsonElement, type: Type?, jdc: JsonDeserializationContext?): LocalDate? {
        return if (je.asString.isEmpty()) null else LocalDate.parse(je.asString, LOCAL_DATE_FORMAT)
    }

    override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(if (src == null) "" else src.toString(LOCAL_DATE_FORMAT))
    }

    companion object {
        private val LOCAL_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd")
    }
}