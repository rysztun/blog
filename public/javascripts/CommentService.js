postControllers.factory('CommentService', [
    '$resource', function ($resource) {
        return $resource('', {}, {

            getComments: {
                params: {id: "@id"},
                method: 'GET',
                url: '/comments/:id',
                isArray: true
            },

            addComment: {
                method: 'POST',
                url: '/comment'
            }
        });
    }]);