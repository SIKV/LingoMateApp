package sikv.lingomate.di

import org.koin.dsl.module
import sikv.lingomate.captureSentryFeedback
import sikv.lingomate.feature.feedback.FeedbackSender

val feedbackSenderModule = module {

    single<FeedbackSender> {
        FeedbackSender(::captureSentryFeedback)
    }
}
