(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingDetailController', VotingDetailController);

    VotingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Voting', 'VotingContent', 'EncryptionData', 'Vote', 'User'];

    function VotingDetailController($scope, $rootScope, $stateParams, previousState, entity, Voting, VotingContent, EncryptionData, Vote, User) {
        var vm = this;

        vm.voting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:votingUpdate', function(event, result) {
            vm.voting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
