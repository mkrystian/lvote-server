(function () {
    'use strict';

    angular
        .module('lvoteApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('voting-answer', {
                parent: 'entity',
                url: '/voting-answer',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lvoteApp.votingAnswer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/voting-answer/voting-answers.html',
                        controller: 'VotingAnswerController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('votingAnswer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('voting-answer-detail', {
                parent: 'voting-answer',
                url: '/voting-answer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'lvoteApp.votingAnswer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/voting-answer/voting-answer-detail.html',
                        controller: 'VotingAnswerDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('votingAnswer');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'VotingAnswer', function ($stateParams, VotingAnswer) {
                        return VotingAnswer.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'voting-answer',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('voting-answer-detail.edit', {
                parent: 'voting-answer-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/voting-answer/voting-answer-dialog.html',
                        controller: 'VotingAnswerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VotingAnswer', function (VotingAnswer) {
                                return VotingAnswer.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('voting-answer.new', {
                parent: 'voting-answer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/voting-answer/voting-answer-dialog.html',
                        controller: 'VotingAnswerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    answer: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('voting-answer', null, {reload: 'voting-answer'});
                    }, function () {
                        $state.go('voting-answer');
                    });
                }]
            })
            .state('voting-answer.edit', {
                parent: 'voting-answer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/voting-answer/voting-answer-dialog.html',
                        controller: 'VotingAnswerDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VotingAnswer', function (VotingAnswer) {
                                return VotingAnswer.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('voting-answer', null, {reload: 'voting-answer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('voting-answer.delete', {
                parent: 'voting-answer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/voting-answer/voting-answer-delete-dialog.html',
                        controller: 'VotingAnswerDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['VotingAnswer', function (VotingAnswer) {
                                return VotingAnswer.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('voting-answer', null, {reload: 'voting-answer'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
