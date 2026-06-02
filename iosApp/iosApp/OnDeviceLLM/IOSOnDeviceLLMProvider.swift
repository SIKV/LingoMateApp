import Foundation
import FoundationModels
import Shared

class IOSOnDeviceLLMProvider : IOSOnDeviceLLMProtocol {
    
    func checkAvailability() async throws -> KotlinBoolean {
        if #available(iOS 26.0, *) {
            let model = SystemLanguageModel.default
            return KotlinBoolean(bool: model.isAvailable)
        } else {
            return false
        }
    }
    
    func streamResponse(input: String, instructions: String) async -> String? {
        if #available(iOS 26.0, *) {
            let model = SystemLanguageModel.default
            
            if !model.isAvailable {
                return nil
            }
            
            let session = LanguageModelSession(
                model: model,
                instructions: "" // TODO: Provide.
            ) 
            
            do {
                let response = try await session.respond(to: "") // TODO: Provide.
                return response.content.capitalized
            } catch {
                return nil
            }
        
        } else {
            return nil
        }
    }
}
