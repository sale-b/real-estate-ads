const state = {
    userId: localStorage.userId || null,
    apiUrl: 'http://client-server:8081'
};

const getters = {
    userId: (state) => state.userId,
    apiUrl: (state) => state.apiUrl
};

const actions = {
    setId(context, id) {
        console.log("SETTING ID TO: " + id )
        localStorage.userId = id;
        context.commit('setId', id);
    },
    removeId(context) {
        localStorage.removeItem('userId');
        context.commit('removeId');
    }
};

const mutations = {
    setId: (state, id) => (state.userId = id),
    removeId: (state) => (state.userId = null)
};

export default {
    state,
    getters,
    actions,
    mutations
}