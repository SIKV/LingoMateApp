import SwiftUI

struct SectionView<Content: View>: View {
    let title: LocalizedStringKey
    let content: Content

    init(_ title: LocalizedStringKey, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .font(.callout)
                .fontWeight(.semibold)
                .foregroundColor(.secondary)
                .padding(.vertical)
            
            content
        }
        .frame(minWidth: 0, maxWidth: .infinity, alignment: .leading)
    }
}
