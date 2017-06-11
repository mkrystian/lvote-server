(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingDialogController', VotingDialogController);

    VotingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Voting', 'VotingContent', 'EncryptionData', 'Vote', 'User', 'UserGroup'];

    function VotingDialogController($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Voting, VotingContent, EncryptionData, Vote, User, UserGroup) {
        var vm = this;

        vm.voting = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.contents = VotingContent.query({filter: 'voting-is-null'});
        $q.all([vm.voting.$promise, vm.contents.$promise]).then(function () {
            if (!vm.voting.content || !vm.voting.content.id) {
                return $q.reject();
            }
            return VotingContent.get({id: vm.voting.content.id}).$promise;
        }).then(function (content) {
            vm.contents.push(content);
        });
        vm.encryptions = EncryptionData.query({filter: 'voting-is-null'});
        $q.all([vm.voting.$promise, vm.encryptions.$promise]).then(function() {
            if (!vm.voting.encryption || !vm.voting.encryption.id) {
                return $q.reject();
            }
            return EncryptionData.get({id : vm.voting.encryption.id}).$promise;
        }).then(function(encryption) {
            vm.encryptions.push(encryption);
        });
        vm.votes = Vote.query();
        vm.users = User.query();
        vm.usergroups = UserGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.voting.id !== null) {
                Voting.update(vm.voting, onSaveSuccess, onSaveError);
            } else {
                Voting.save(vm.voting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lvoteApp:votingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
