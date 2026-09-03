package sikv.lingomate.data.config.domain

/**
 * Fills in the sections the remote config leaves out, so a config that carries only some of
 * them is still usable. Empty and missing mean the same thing here.
 */
internal fun Config.withFallback(fallback: Config): Config {
    return Config(
        chatModels = chatModels.ifEmpty { fallback.chatModels },
        languageCodes = languageCodes.ifEmpty { fallback.languageCodes }
    )
}
