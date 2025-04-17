<template>
  <v-app>
    <Header @toggle-drawer="drawer = !drawer" />

    <Sidebar v-if="isAuthenticated" v-model="drawer" />

    <v-main>
      <router-view></router-view>
    </v-main>

    <FooterComponent />

    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="snackbar.timeout"
    >
      {{ snackbar.text }}

      <template v-slot:action="{ attrs }">
        <v-btn text v-bind="attrs" @click="snackbar.show = false">
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </v-app>
</template>

<script>
import Header from "@/components/common/Header.vue";
import Sidebar from "@/components/common/Sidebar.vue";
import FooterComponent from "@/components/common/FooterComponent.vue";
import { mapGetters } from "vuex";

export default {
  name: "App",

  components: {
    Header,
    Sidebar,
    FooterComponent,
  },

  data: () => ({
    drawer: false,
    snackbar: {
      show: false,
      text: "",
      color: "info",
      timeout: 3000,
    },
  }),

  computed: {
    ...mapGetters("user", ["isAuthenticated", "currentUser"]),
  },

  created() {
    // 检查是否已登录，如果有token但没有用户信息，则获取用户信息
    if (this.isAuthenticated && !this.currentUser) {
      this.$store.dispatch("user/fetchCurrentUser");
    }

    // 连接WebSocket
    if (this.isAuthenticated) {
      this.$socket.connect();
    }

    // 添加全局通知方法
    this.$notify = this.showNotification;
  },

  watch: {
    isAuthenticated(newValue) {
      // 当登录状态变化时连接/断开WebSocket
      if (newValue) {
        this.$socket.connect();
      } else {
        this.$socket.disconnect();
      }
    },
  },

  methods: {
    showNotification(text, options = {}) {
      this.snackbar.text = text;
      this.snackbar.color = options.color || "info";
      this.snackbar.timeout = options.timeout || 3000;
      this.snackbar.show = true;
    },
  },

  // WebSocket事件处理
  sockets: {
    connect() {
      if (this.currentUser) {
        // 发送认证信息
        this.$socket.emit("authenticate", {
          userId: this.currentUser.userId,
          token: this.$store.getters["user/token"],
        });
      }
    },

    notification(data) {
      // 接收到新通知
      this.$store.dispatch("notification/receiveNotification", data);

      if (data.type !== "MESSAGE") {
        // 聊天消息由Chat组件单独处理
        this.showNotification(data.title, { color: "info" });
      }
    },

    chat_message(data) {
      // 接收到聊天消息
      this.$store.dispatch("chat/receiveMessage", data);
    },
  },
};
</script>

<style>
html {
  overflow-y: auto;
  scroll-behavior: smooth;
}

.page-enter-active,
.page-leave-active {
  transition: opacity 0.3s;
}
.page-enter,
.page-leave-to {
  opacity: 0;
}

.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.v-application {
  font-family: "Roboto", sans-serif;
}
</style>
