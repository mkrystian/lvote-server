(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VoteDetailController', VoteDetailController);

    VoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vote'];

    function VoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Vote) {
        var vm = this;

        vm.vote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:voteUpdate', function(event, result) {
            vm.vote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
