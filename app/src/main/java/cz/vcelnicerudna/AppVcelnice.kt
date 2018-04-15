package cz.vcelnicerudna

import android.app.Application
import cz.vcelnicerudna.configuration.APIConstants
import org.acra.ACRA
import org.acra.ReportField
import org.acra.annotation.AcraHttpSender
import org.acra.config.ACRAConfigurationException
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.DialogConfigurationBuilder
import org.acra.data.StringFormat
import org.acra.sender.HttpSender

@Suppress("unused")
@AcraHttpSender(uri = APIConstants.RESERVE_POST_URL,
        httpMethod = HttpSender.Method.POST)
class AppVcelnice : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            val builder = CoreConfigurationBuilder(this)
                    .setBuildConfigClass(BuildConfig::class.java)
                    .setReportFormat(StringFormat.JSON)
                    .setReportContent(
                            ReportField.STACK_TRACE,
                            ReportField.APP_VERSION_CODE,
                            ReportField.ANDROID_VERSION,
                            ReportField.PHONE_MODEL,
                            ReportField.USER_COMMENT)
            builder
                    .getPluginConfigurationBuilder(DialogConfigurationBuilder::class.java)
                    .setResText(R.string.crash_text)
                    .setResTitle(R.string.crash_title)
                    .setResCommentPrompt(R.string.crash_prompt)
                    .setResNegativeButtonText(R.string.no)
                    .setResPositiveButtonText(R.string.yes)
                    .setEnabled(true)
            ACRA.init(this, builder)
        } catch (e: ACRAConfigurationException) {
            e.printStackTrace()
        }
    }
}