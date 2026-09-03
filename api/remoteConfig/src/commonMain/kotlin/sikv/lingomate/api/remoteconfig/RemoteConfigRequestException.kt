package sikv.lingomate.api.remoteconfig

class RemoteConfigRequestException(statusCode: Int) : Exception("Config request failed: $statusCode.")
