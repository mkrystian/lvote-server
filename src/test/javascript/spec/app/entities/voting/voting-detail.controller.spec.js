'use strict';

describe('Controller Tests', function() {

    describe('Voting Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVoting, MockVotingContent, MockEncryptionData, MockUser, MockUserGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVoting = jasmine.createSpy('MockVoting');
            MockVotingContent = jasmine.createSpy('MockVotingContent');
            MockEncryptionData = jasmine.createSpy('MockEncryptionData');
            MockUser = jasmine.createSpy('MockUser');
            MockUserGroup = jasmine.createSpy('MockUserGroup');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Voting': MockVoting,
                'VotingContent': MockVotingContent,
                'EncryptionData': MockEncryptionData,
                'User': MockUser,
                'UserGroup': MockUserGroup
            };
            createController = function() {
                $injector.get('$controller')("VotingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lvoteApp:votingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
