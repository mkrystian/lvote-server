(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('encryption-data', {
            parent: 'entity',
            url: '/encryption-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.encryptionData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/encryption-data/encryption-data.html',
                    controller: 'EncryptionDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('encryptionData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('encryption-data-detail', {
            parent: 'encryption-data',
            url: '/encryption-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.encryptionData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/encryption-data/encryption-data-detail.html',
                    controller: 'EncryptionDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('encryptionData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EncryptionData', function($stateParams, EncryptionData) {
                    return EncryptionData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'encryption-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('encryption-data-detail.edit', {
            parent: 'encryption-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/encryption-data/encryption-data-dialog.html',
                    controller: 'EncryptionDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EncryptionData', function(EncryptionData) {
                            return EncryptionData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('encryption-data.new', {
            parent: 'encryption-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/encryption-data/encryption-data-dialog.html',
                    controller: 'EncryptionDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                privateKey: null,
                                publicKey: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('encryption-data', null, { reload: 'encryption-data' });
                }, function() {
                    $state.go('encryption-data');
                });
            }]
        })
        .state('encryption-data.edit', {
            parent: 'encryption-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/encryption-data/encryption-data-dialog.html',
                    controller: 'EncryptionDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EncryptionData', function(EncryptionData) {
                            return EncryptionData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('encryption-data', null, { reload: 'encryption-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('encryption-data.delete', {
            parent: 'encryption-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/encryption-data/encryption-data-delete-dialog.html',
                    controller: 'EncryptionDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EncryptionData', function(EncryptionData) {
                            return EncryptionData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('encryption-data', null, { reload: 'encryption-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
