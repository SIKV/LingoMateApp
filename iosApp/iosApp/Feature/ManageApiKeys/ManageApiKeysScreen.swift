import SwiftUI
import Shared

struct ManageApiKeysScreen: View {
    @StateObject private var manageApiKeysVM = ManageApiKeysVM()

    @State private var showAddSheet = false
    @State private var providersPendingDeletion: [ApiKeyProvider] = []

    private var storedProviders: [ApiKeyProvider] {
        manageApiKeysVM.state.storedProviders
    }

    private var showsDeleteConfirmation: Binding<Bool> {
        Binding(
            get: { !providersPendingDeletion.isEmpty },
            set: { isPresented in
                if !isPresented {
                    providersPendingDeletion = []
                }
            }
        )
    }

    var body: some View {
        Group {
            if storedProviders.isEmpty {
                emptyState
            } else {
                keyList
            }
        }
        .navigationTitle(L10n.manageApiKeysTitle)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            if !storedProviders.isEmpty {
                ToolbarItem(placement: .topBarLeading) {
                    EditButton()
                }
            }

            ToolbarItem(placement: .topBarTrailing) {
                Button {
                    showAddSheet = true
                } label: {
                    Label(L10n.manageApiKeysAddButton, systemImage: "plus")
                }
            }
        }
        .sheet(isPresented: $showAddSheet) {
            AddApiKeySheet(
                storedProviders: storedProviders,
                onAdd: manageApiKeysVM.addApiKey
            )
        }
        .confirmationDialog(
            L10n.manageApiKeysDeleteConfirmTitle,
            isPresented: showsDeleteConfirmation,
            titleVisibility: .visible
        ) {
            Button(L10n.manageApiKeysDeleteButton, role: .destructive) {
                providersPendingDeletion.forEach(manageApiKeysVM.removeApiKey)
                providersPendingDeletion = []
            }

            Button(L10n.manageApiKeysCancelButton, role: .cancel) { }
        } message: {
            Text(
                L10n.manageApiKeysDeleteConfirmMessage(
                    providersPendingDeletion
                        .map(\.localizedNameString)
                        .joined(separator: ", ")
                )
            )
        }
        .task {
            manageApiKeysVM.listenState()
        }
        .onDisappear {
            manageApiKeysVM.cancel()
        }
    }

    // MARK: - Key list

    private var keyList: some View {
        List {
            Section {
                ForEach(storedProviders, id: \.storageKey) { provider in
                    ApiKeyRow(provider: provider)
                }
                // Deleting a key is irreversible, so the swipe only stages it.
                .onDelete { offsets in
                    providersPendingDeletion = offsets.map { storedProviders[$0] }
                }
            } footer: {
                Text(L10n.manageApiKeysListFooter)
            }
        }
    }

    // MARK: - Empty state

    private var emptyState: some View {
        ContentUnavailableView {
            Label(L10n.manageApiKeysEmptyTitle, systemImage: "key")
        } description: {
            Text(L10n.manageApiKeysEmptyInfo)
        } actions: {
            Button(L10n.manageApiKeysAddButton) {
                showAddSheet = true
            }
        }
    }
}

// MARK: - Key row

private struct ApiKeyRow: View {
    let provider: ApiKeyProvider

    var body: some View {
        Label {
            VStack(alignment: .leading, spacing: Spacing.xs) {
                Text(provider.localizedName)

                Text(L10n.manageApiKeysKeySaved)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
        } icon: {
            Image(systemName: "key.fill")
                .foregroundStyle(Color.accentColor)
        }
    }
}

// MARK: - Add key sheet

private struct AddApiKeySheet: View {
    let storedProviders: [ApiKeyProvider]
    let onAdd: (ApiKeyProvider, String) -> Void

    @Environment(\.dismiss) private var dismiss

    @State private var selectedProvider: ApiKeyProvider
    @State private var apiKey = ""
    @State private var keyVisible = false
    @State private var showReplaceConfirmation = false

    @FocusState private var keyFieldFocused: Bool

    private var trimmedApiKey: String {
        apiKey.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    private var replacesExistingKey: Bool {
        storedProviders.contains(selectedProvider)
    }

    init(storedProviders: [ApiKeyProvider], onAdd: @escaping (ApiKeyProvider, String) -> Void) {
        self.storedProviders = storedProviders
        self.onAdd = onAdd

        // Preselect a provider the user has not stored a key for yet.
        _selectedProvider = State(
            initialValue: ApiKeyProvider.entries.first { !storedProviders.contains($0) }
                ?? ApiKeyProvider.entries[0]
        )
    }

    var body: some View {
        NavigationStack {
            Form {
                Section {
                    Picker(L10n.manageApiKeysProviderLabel, selection: $selectedProvider) {
                        ForEach(ApiKeyProvider.entries, id: \.storageKey) { provider in
                            Text(provider.localizedName).tag(provider)
                        }
                    }
                }

                Section {
                    keyField
                } footer: {
                    Text(L10n.manageApiKeysKeyFooter)
                }
            }
            .navigationTitle(
                replacesExistingKey ? L10n.manageApiKeysReplaceTitle : L10n.manageApiKeysAddTitle
            )
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(L10n.manageApiKeysCancelButton) {
                        dismiss()
                    }
                }

                ToolbarItem(placement: .confirmationAction) {
                    Button(L10n.manageApiKeysSaveButton) {
                        // Overwriting a stored key destroys it, so confirm first.
                        if replacesExistingKey {
                            showReplaceConfirmation = true
                        } else {
                            save()
                        }
                    }
                    .disabled(trimmedApiKey.isEmpty)
                }
            }
            .alert(L10n.manageApiKeysReplaceConfirmTitle, isPresented: $showReplaceConfirmation) {
                Button(L10n.manageApiKeysReplaceConfirmButton, role: .destructive) {
                    save()
                }

                // Leaves the entered key intact so it can be saved after all.
                Button(L10n.manageApiKeysCancelButton, role: .cancel) { }
            } message: {
                Text(L10n.manageApiKeysReplaceConfirmMessage(selectedProvider.localizedNameString))
            }
        }
        .presentationDetents([.medium, .large])
        .onAppear {
            keyFieldFocused = true
        }
    }

    private var keyField: some View {
        HStack {
            Group {
                if keyVisible {
                    TextField(L10n.manageApiKeysKeyLabel, text: $apiKey)
                } else {
                    SecureField(L10n.manageApiKeysKeyLabel, text: $apiKey)
                }
            }
            .textInputAutocapitalization(.never)
            .autocorrectionDisabled()
            .focused($keyFieldFocused)

            Button {
                keyVisible.toggle()
            } label: {
                Image(systemName: keyVisible ? "eye.slash" : "eye")
                    .foregroundStyle(.secondary)
            }
            // Keeps the tap from activating the whole row.
            .buttonStyle(.borderless)
            .accessibilityLabel(keyVisible ? L10n.manageApiKeysHideKey : L10n.manageApiKeysShowKey)
        }
    }

    private func save() {
        onAdd(selectedProvider, trimmedApiKey)
        dismiss()
    }
}
