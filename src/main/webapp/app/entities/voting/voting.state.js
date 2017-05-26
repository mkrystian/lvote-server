(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('voting', {
            parent: 'entity',
            url: '/voting',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.voting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voting/votings.html',
                    controller: 'VotingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('voting');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('voting-detail', {
            parent: 'voting',
            url: '/voting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.voting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voting/voting-detail.html',
                    controller: 'VotingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('voting');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Voting', function($stateParams, Voting) {
                    return Voting.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'voting',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('voting-detail.edit', {
            parent: 'voting-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting/voting-dialog.html',
                    controller: 'VotingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Voting', function(Voting) {
                            return Voting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voting.new', {
            parent: 'voting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting/voting-dialog.html',
                    controller: 'VotingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                startDateTime: null,
                                endDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('voting', null, { reload: 'voting' });
                }, function() {
                    $state.go('voting');
                });
            }]
        })
        .state('voting.edit', {
            parent: 'voting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting/voting-dialog.html',
                    controller: 'VotingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Voting', function(Voting) {
                            return Voting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voting', null, { reload: 'voting' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voting.delete', {
            parent: 'voting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting/voting-delete-dialog.html',
                    controller: 'VotingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Voting', function(Voting) {
                            return Voting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voting', null, { reload: 'voting' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
