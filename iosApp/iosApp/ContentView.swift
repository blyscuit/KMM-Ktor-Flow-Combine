import SwiftUI
import shared
import Kingfisher
import Combine

extension Pokemon: Identifiable {}

struct ListView: View {
    let pokemon: Pokemon

    var body: some View {
        if #available(iOS 15.0, *) {
            VStack {
                Text(pokemon.name)
                KFImage(URL(string: pokemon.frontImage)!)
            }
            .overlay {
                NavigationLink {
                    DetailView(url: pokemon.url)
                } label: {
                    ZStack{}
                }
            }
        } else {
            VStack{}
            // Fallback on earlier versions
        }
    }
}

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel

    var body: some View {
        NavigationView {
            listView()
                .navigationBarTitle("Pokedex")
                .navigationBarItems(trailing:
                                        Button("Reload") {
                    self.viewModel.loadLaunches()
                })
        }
    }

    private func listView() -> AnyView {
        switch viewModel.pokemons {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let pokemons):
            return AnyView(List(pokemons) { pokemon in
                ListView(pokemon: pokemon)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {
    enum LoadableLaunches {
        case loading
        case result([Pokemon])
        case error(String)
    }

    class ViewModel: ObservableObject {
        @Published var pokemons = LoadableLaunches.loading
        var cancels = Set<AnyCancellable>()
        let sdk: PokemonSDK

        init(sdk: PokemonSDK) {
            self.sdk = sdk
            self.loadLaunches()
        }

        func loadLaunches() {
            self.pokemons = .loading
            CommonFlowWrapper.publisher(sdk, PokemonList.self, PokemonSDK.PokemonFlowDex())
                .sink { completion in
                    switch completion {
                    case let .failure(error):
                        self.pokemons = .error(error.localizedDescription)
                    default: break
                    }
                } receiveValue: { pokemon in
                    self.pokemons = .result(pokemon.results)
                }
                .store(in: &cancels)
        }
    }
}
