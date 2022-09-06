//
//  SDKCombineWrapper.swift
//  iosApp
//
//  Created by Bliss on 2/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import shared
import Combine

class Collector<T>: Kotlinx_coroutines_coreFlowCollector {

    let callback:((T?, Error?)) -> Void

    init(callback: @escaping ((T?, Error?)) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        guard let value = value as? T else {
            callback((nil, KotlinError.normal))
            completionHandler(nil)
            return
        }
        callback((value, nil))
        completionHandler(nil)
    }
}

enum CommonFlowWrapper {

    static func publisher<T: AnyObject>(_ sdk: PokemonSDK, _ type: T.Type, _ flow: PokemonSDK.PokemonFlow) -> AnyPublisher<T, Error> {
        Deferred {
            Future { completion in
                sdk.getFlowIOS(
                    flow: flow,
                    collector:
                    Collector<T> { value, error in
                        guard let value = value else {
                            completion(.failure(error ?? KotlinError.normal))
                            return
                        }

                        completion(.success(value))
                    }) { error in
                        guard let error = error else { return }
                        completion(.failure(error))
                    }
            }
        }
        .eraseToAnyPublisher()
    }

    static func publisher<T: AnyObject>(_ type: T.Type, _ call: @escaping (Kotlinx_coroutines_coreFlowCollector, @escaping (Error?) -> Void) -> Void) -> AnyPublisher<T, Error> {
        Deferred {
            Future { completion in
                call(
                    Collector<T>{ value, error in
                        guard let value = value else {
                            completion(.failure(error ?? KotlinError.normal))
                            return
                        }
                        
                        completion(.success(value))
                    }) { error in
                        guard let error = error else { return }
                        completion(.failure(error))
                    }
            }
        }
        .eraseToAnyPublisher()
    }
}

enum KotlinError: Error {
    
    case normal
}
