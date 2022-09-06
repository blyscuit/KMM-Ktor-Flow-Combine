//
//  DetailView.swift
//  iosApp
//
//  Created by Bliss on 1/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import Kingfisher
import Combine

struct DetailView: View {
    let sdk = PokemonSDK()
    let url: String
    @ObservedObject var viewModel = ViewModel()

    var body: some View {
        VStack {
            if let pokemon = viewModel.pokemon {
                KFImage(URL(string: pokemon.sprites.front)!)
                Text(pokemon.name)
            } else {
                ZStack{}
            }
        }
        .onAppear {
            viewModel.loadLaunches(url: url)
        }
    }

    class ViewModel: ObservableObject {
        @Published var pokemon: PokemonDetail?
        var cancellables = Set<AnyCancellable>()

        let sdk: PokemonSDK

        init(sdk: PokemonSDK = PokemonSDK()) {
            self.sdk = sdk
        }

        func loadLaunches(url: String) {
            CommonFlowWrapper.publisher(sdk, PokemonDetail.self, PokemonSDK.PokemonFlowDetail(url: url))
            .sink { _ in

            } receiveValue: { pokemon in
                self.pokemon = pokemon
            }
            .store(in: &cancellables)
        }
    }
}
