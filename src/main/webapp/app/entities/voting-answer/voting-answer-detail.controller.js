(function () {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingAnswerDetailController', VotingAnswerDetailController);

    VotingAnswerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VotingAnswer', 'VotingContent'];

    function VotingAnswerDetailController($scope, $rootScope, $stateParams, previousState, entity, VotingAnswer, VotingContent) {
        var vm = this;

        vm.votingAnswer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:votingAnswerUpdate', function (event, result) {
            vm.votingAnswer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
