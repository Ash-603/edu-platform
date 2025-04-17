<template>
    <div>
      <v-carousel
        cycle
        height="500"
        hide-delimiter-background
        show-arrows-on-hover
      >
        <v-carousel-item
          v-for="(item, i) in carouselItems"
          :key="i"
          :src="item.src"
        >
          <v-sheet
            height="100%"
            color="rgba(0, 0, 0, 0.5)"
            class="d-flex align-center justify-center"
          >
            <div class="text-center">
              <h1 class="text-h3 font-weight-bold white--text mb-4">{{ item.title }}</h1>
              <div class="text-h6 white--text mb-6">{{ item.text }}</div>
              <v-btn
                large
                color="primary"
                :to="item.link"
              >
                {{ item.btnText }}
              </v-btn>
            </div>
          </v-sheet>
        </v-carousel-item>
      </v-carousel>
      
      <v-container>
        <section class="my-10">
          <v-row>
            <v-col cols="12" class="text-center mb-6">
              <h2 class="text-h4 font-weight-bold">Why Choose Our Platform?</h2>
              <p class="text-subtitle-1 grey--text text--darken-1">
                Learn from industry experts and advance your career
              </p>
            </v-col>
            
            <v-col
              v-for="(feature, index) in features"
              :key="index"
              cols="12"
              md="4"
              sm="6"
            >
              <v-card height="100%" outlined hover>
                <v-card-text class="text-center">
                  <v-icon x-large color="primary" class="mb-4">{{ feature.icon }}</v-icon>
                  <h3 class="text-h6 font-weight-bold mb-2">{{ feature.title }}</h3>
                  <p>{{ feature.description }}</p>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </section>
        
        <section class="my-10">
          <v-row>
            <v-col cols="12" class="text-center mb-6">
              <h2 class="text-h4 font-weight-bold">Popular Courses</h2>
              <p class="text-subtitle-1 grey--text text--darken-1">
                Discover our most in-demand courses
              </p>
            </v-col>
          </v-row>
          
          <v-row>
            <v-col
              v-for="course in popularCourses"
              :key="course.courseId"
              cols="12"
              md="4"
              sm="6"
            >
              <CourseCard :course="course" />
            </v-col>
          </v-row>
          
          <div class="text-center mt-6">
            <v-btn large color="primary" to="/courses">
              Browse All Courses
            </v-btn>
          </div>
        </section>
        
        <v-divider></v-divider>
        
        <section class="my-10">
          <v-row align="center">
            <v-col cols="12" md="6">
              <h2 class="text-h4 font-weight-bold mb-4">Become an Instructor</h2>
              <p class="text-subtitle-1 mb-4">
                Share your knowledge and expertise with students worldwide. Create engaging courses
                and earn revenue while making a difference in students' lives.
              </p>
              <v-btn large color="secondary" to="/become-instructor">
                Start Teaching
              </v-btn>
            </v-col>
            
            <v-col cols="12" md="6">
              <v-img
                src="@/assets/become-instructor.jpg"
                alt="Become an instructor"
                contain
                max-height="400"
              ></v-img>
            </v-col>
          </v-row>
        </section>
        
        <section class="my-10">
          <v-row>
            <v-col cols="12" class="text-center mb-6">
              <h2 class="text-h4 font-weight-bold">What Our Students Say</h2>
            </v-col>
          </v-row>
          
          <v-row>
            <v-col
              v-for="(testimonial, index) in testimonials"
              :key="index"
              cols="12"
              md="4"
            >
              <v-card class="mx-auto" height="100%" outlined>
                <v-card-text>
                  <div class="text-h6 text-center">
                    <v-icon color="amber">mdi-star</v-icon>
                    <v-icon color="amber">mdi-star</v-icon>
                    <v-icon color="amber">mdi-star</v-icon>
                    <v-icon color="amber">mdi-star</v-icon>
                    <v-icon color="amber">mdi-star</v-icon>
                  </div>
                  <blockquote class="blockquote text-center">
                    {{ testimonial.quote }}
                  </blockquote>
                </v-card-text>
                
                <v-card-actions class="justify-center">
                  <v-list-item class="grow">
                    <v-list-item-avatar>
                      <v-img :src="testimonial.avatar"></v-img>
                    </v-list-item-avatar>
                    
                    <v-list-item-content>
                      <v-list-item-title>{{ testimonial.name }}</v-list-item-title>
                      <v-list-item-subtitle>{{ testimonial.role }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </section>
      </v-container>
    </div>
  </template>
  
  <script>
  import CourseCard from '@/components/course/CourseCard.vue'
  
  export default {
    components: {
      CourseCard
    },
    
    data() {
      return {
        carouselItems: [
          {
            src: require('@/assets/carousel1.jpg'),
            title: 'Learn From The Best',
            text: 'Access high-quality courses taught by industry experts',
            link: '/courses',
            btnText: 'Explore Courses'
          },
          {
            src: require('@/assets/carousel2.jpg'),
            title: 'Learn At Your Own Pace',
            text: 'Access courses anytime, anywhere, and learn at your own pace',
            link: '/register',
            btnText: 'Join Now'
          },
          {
            src: require('@/assets/carousel3.jpg'),
            title: 'Advance Your Career',
            text: 'Gain valuable skills that will help you advance in your career',
            link: '/courses',
            btnText: 'Get Started'
          }
        ],
        
        features: [
          {
            icon: 'mdi-quality-high',
            title: 'High-Quality Content',
            description: 'Our courses are created by industry experts with years of experience.'
          },
          {
            icon: 'mdi-clock-outline',
            title: 'Learn at Your Own Pace',
            description: 'Access course materials anytime, anywhere, and learn at your convenience.'
          },
          {
            icon: 'mdi-certificate',
            title: 'Get Certified',
            description: 'Earn certificates upon course completion to showcase your new skills.'
          },
          {
            icon: 'mdi-account-group',
            title: 'Community Support',
            description: 'Join a community of learners and instructors for networking and support.'
          },
          {
            icon: 'mdi-update',
            title: 'Regular Updates',
            description: 'Our courses are regularly updated to reflect the latest industry trends.'
          },
          {
            icon: 'mdi-hand-coin',
            title: 'Affordable Pricing',
            description: 'Access quality education at affordable prices with frequent discounts.'
          }
        ],
        
        popularCourses: [],
        
        testimonials: [
          {
            quote: 'The courses on this platform have helped me advance my career in ways I never thought possible. The instructors are knowledgeable and the content is top-notch.',
            name: 'Sarah Johnson',
            role: 'Software Developer',
            avatar: 'https://randomuser.me/api/portraits/women/44.jpg'
          },
          {
            quote: 'I\'ve tried many online learning platforms, but this one stands out for its quality and instructor support. Highly recommended!',
            name: 'Michael Brown',
            role: 'Marketing Manager',
            avatar: 'https://randomuser.me/api/portraits/men/40.jpg'
          },
          {
            quote: 'The flexibility of learning at my own pace combined with the comprehensive curriculum made this the perfect platform for me to gain new skills.',
            name: 'Emma Wilson',
            role: 'UX Designer',
            avatar: 'https://randomuser.me/api/portraits/women/28.jpg'
          }
        ]
      }
    },
    
    created() {
      this.fetchPopularCourses()
    },
    
    methods: {
      async fetchPopularCourses() {
        try {
          const response = await this.$axios.get('/courses/popular')
          this.popularCourses = response.data.slice(0, 6) // 只显示前6个
        } catch (error) {
          console.error('Failed to fetch popular courses:', error)
        }
      }
    }
  }
  </script>