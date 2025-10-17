import SwiftUI

final class AppRouter: ObservableObject {
    @Published var path = NavigationPath()
    
    func navigate(to: Route) {
        path.append(to)
    }
    
    func navigateBack() {
        path.removeLast()
    }
    
    func navigateToRoot() {
        path.removeLast(path.count)
    }
    
    func reset() {
        path = NavigationPath()
    }
}
