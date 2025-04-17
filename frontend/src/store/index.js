import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

// 模块
import userModule from "./modules/user";
import courseModule from "./modules/course";
import chatModule from "./modules/chat";
import notificationModule from "./modules/notification";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    user: userModule,
    course: courseModule,
    chat: chatModule,
    notification: notificationModule,
  },
  plugins: [
    createPersistedState({
      key: "edu-platform",
      paths: ["user.token", "user.currentUser"],
    }),
  ],
});
