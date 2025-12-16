package sikv.lingomate.data.chat.domain

internal enum class ChatResponseChunkType {
    Error,
    Created,
    InProgress,
    Completed,
    Failed,
    Incomplete,
    OutputItemAdded,
    OutputItemDone,
    ContentPartAdded,
    ContentPartDone,
    OutputTextDelta,
    OutputTextDone,
}
