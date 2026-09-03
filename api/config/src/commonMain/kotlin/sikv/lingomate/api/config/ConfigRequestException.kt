package sikv.lingomate.api.config

class ConfigRequestException(statusCode: Int) : Exception("Config request failed: $statusCode.")
