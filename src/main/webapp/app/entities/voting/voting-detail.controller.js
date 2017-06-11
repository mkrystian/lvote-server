(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingDetailController', VotingDetailController);

    VotingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Voting', 'VotingContent', 'EncryptionData', 'Vote', 'User', 'UserGroup'];

    function VotingDetailController($scope, $rootScope, $stateParams, previousState, entity, Voting, VotingContent, EncryptionData, Vote, User, UserGroup) {
        var vm = this;

        vm.voting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:votingUpdate', function(event, result) {
            vm.voting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
