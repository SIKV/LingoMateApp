import SwiftUI

final class AppRouter: ObservableObject {
    @Published var path = NavigationPath()
    
    func navigate(to: Route) {
        path.append(to)
    }
    
    func navigateBack() {
        path.removeLast()
    }
    
    func replaceLast(with route: Route) {
        path.removeLast()
        path.append(route)
    }
    
    func navigateToRoot() {
        path.removeLast(path.count)
    }
    
    func reset() {
        path = NavigationPath()
    }
}
