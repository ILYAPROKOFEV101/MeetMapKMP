import com.ilya.platform.DriverFactory
import com.ilya.platform.createDatabase
import org.koin.dsl.module

val platformModule = module {
    single { createDatabase(DriverFactory(get())) }
}