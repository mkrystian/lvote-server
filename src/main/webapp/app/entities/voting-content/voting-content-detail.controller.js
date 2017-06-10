(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingContentDetailController', VotingContentDetailController);

    VotingContentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VotingContent', 'Voting'];

    function VotingContentDetailController($scope, $rootScope, $stateParams, previousState, entity, VotingContent, Voting) {
        var vm = this;

        vm.votingContent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:votingContentUpdate', function(event, result) {
            vm.votingContent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
