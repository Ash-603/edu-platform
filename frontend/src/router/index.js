import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

// 页面组件
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import CourseList from '../views/CourseList.vue'
import CourseDetail from '../views/CourseDetail.vue'
import Assignment from '../views/Assignment.vue'
import Payment from '../views/Payment.vue'
import Chat from '../views/Chat.vue'
import UserDashboard from '../views/UserDashboard.vue'
import TeacherDashboard from '../views/TeacherDashboard.vue'
import CourseCreation from '../views/CourseCreation.vue'
import Profile from '../views/Profile.vue'
import AssignmentGrading from '../views/AssignmentGrading.vue'
import NotFound from '../views/NotFound.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { guestOnly: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { guestOnly: true }
  },
  {
    path: '/courses',
    name: 'CourseList',
    component: CourseList
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: CourseDetail,
    props: true
  },
  {
    path: '/assignments/:id',
    name: 'Assignment',
    component: Assignment,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/payment/:orderId',
    name: 'Payment',
    component: Payment,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/chat/:roomId?',
    name: 'Chat',
    component: Chat,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/dashboard',
    name: 'UserDashboard',
    component: UserDashboard,
    meta: { requiresAuth: true, studentOnly: true }
  },
  {
    path: '/teacher/dashboard',
    name: 'TeacherDashboard',
    component: TeacherDashboard,
    meta: { requiresAuth: true, teacherOnly: true }
  },
  {
    path: '/teacher/courses/create',
    name: 'CourseCreation',
    component: CourseCreation,
    meta: { requiresAuth: true, teacherOnly: true }
  },
  {
    path: '/teacher/assignments/:id/grade',
    name: 'AssignmentGrading',
    component: AssignmentGrading,
    props: true,
    meta: { requiresAuth: true, teacherOnly: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '*',
    name: 'NotFound',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior() {
    return { x: 0, y: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = store.getters.isAuthenticated
  const isTeacher = store.getters.isTeacher
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const guestOnly = to.matched.some(record => record.meta.guestOnly)
  const teacherOnly = to.matched.some(record => record.meta.teacherOnly)
  const studentOnly = to.matched.some(record => record.meta.studentOnly)

  if (requiresAuth && !isLoggedIn) {
    next('/login')
  } else if (guestOnly && isLoggedIn) {
    next('/dashboard')
  } else if (teacherOnly && !isTeacher) {
    next('/dashboard')
  } else if (studentOnly && isTeacher) {
    next('/teacher/dashboard')
  } else {
    next()
  }
})

export default router