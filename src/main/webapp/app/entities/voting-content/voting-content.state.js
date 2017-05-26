(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('voting-content', {
            parent: 'entity',
            url: '/voting-content',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.votingContent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voting-content/voting-contents.html',
                    controller: 'VotingContentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('votingContent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('voting-content-detail', {
            parent: 'voting-content',
            url: '/voting-content/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lvoteApp.votingContent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/voting-content/voting-content-detail.html',
                    controller: 'VotingContentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('votingContent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VotingContent', function($stateParams, VotingContent) {
                    return VotingContent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'voting-content',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('voting-content-detail.edit', {
            parent: 'voting-content-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting-content/voting-content-dialog.html',
                    controller: 'VotingContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VotingContent', function(VotingContent) {
                            return VotingContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voting-content.new', {
            parent: 'voting-content',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting-content/voting-content-dialog.html',
                    controller: 'VotingContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                question: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('voting-content', null, { reload: 'voting-content' });
                }, function() {
                    $state.go('voting-content');
                });
            }]
        })
        .state('voting-content.edit', {
            parent: 'voting-content',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting-content/voting-content-dialog.html',
                    controller: 'VotingContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VotingContent', function(VotingContent) {
                            return VotingContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voting-content', null, { reload: 'voting-content' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('voting-content.delete', {
            parent: 'voting-content',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/voting-content/voting-content-delete-dialog.html',
                    controller: 'VotingContentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VotingContent', function(VotingContent) {
                            return VotingContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('voting-content', null, { reload: 'voting-content' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
