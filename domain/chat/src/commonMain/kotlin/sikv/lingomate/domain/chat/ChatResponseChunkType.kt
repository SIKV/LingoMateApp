package sikv.lingomate.domain.chat

enum class ChatResponseChunkType {
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
