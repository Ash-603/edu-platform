<template>
  <div>
    <div v-if="loading" class="text-center my-10">
      <v-progress-circular
        indeterminate
        color="primary"
        size="64"
      ></v-progress-circular>
    </div>

    <div v-else>
      <v-container v-if="course">
        <v-row>
          <v-col cols="12" md="8">
            <v-card>
              <v-img
                :src="
                  course.thumbnail || require('@/assets/course-placeholder.jpg')
                "
                height="300"
                class="white--text align-end"
              >
                <div class="pa-4 bg-black-transparent">
                  <div class="text-h4 font-weight-bold">{{ course.title }}</div>
                  <div class="d-flex align-center mt-2">
                    <v-chip color="primary" small class="mr-2">{{
                      course.category
                    }}</v-chip>
                    <v-chip
                      :color="getLevelColor(course.level)"
                      small
                      class="mr-2"
                      >{{ course.level }}</v-chip
                    >
                    <v-rating
                      :value="course.rating || 0"
                      color="amber"
                      dense
                      half-increments
                      readonly
                      size="18"
                    ></v-rating>
                    <span class="ml-2 white--text"
                      >{{ course.ratingCount || 0 }} ratings</span
                    >
                  </div>
                </div>
              </v-img>

              <v-card-text>
                <div class="d-flex align-center mb-4">
                  <v-avatar size="40" class="mr-3">
                    <v-img
                      :src="
                        course.teacherAvatar || 'https://via.placeholder.com/50'
                      "
                    ></v-img>
                  </v-avatar>
                  <div>
                    <div class="text-subtitle-1 font-weight-medium">
                      {{ course.teacherName }}
                    </div>
                    <div class="text-caption">Instructor</div>
                  </div>
                </div>

                <v-tabs v-model="activeTab">
                  <v-tab>Overview</v-tab>
                  <v-tab>Curriculum</v-tab>
                  <v-tab>Materials</v-tab>
                  <v-tab>Reviews</v-tab>
                  <v-tab>Announcements</v-tab>
                </v-tabs>

                <v-tabs-items v-model="activeTab">
                  <!-- Overview Tab -->
                  <v-tab-item>
                    <div class="py-4">
                      <h3 class="text-h6 font-weight-bold mb-3">
                        About This Course
                      </h3>
                      <p>{{ course.description }}</p>

                      <h3 class="text-h6 font-weight-bold mt-5 mb-3">
                        What You'll Learn
                      </h3>
                      <v-row>
                        <v-col
                          cols="12"
                          md="6"
                          v-for="(item, index) in course.learningObjectives ||
                          []"
                          :key="index"
                        >
                          <div class="d-flex align-start">
                            <v-icon color="success" class="mr-2"
                              >mdi-check-circle</v-icon
                            >
                            <span>{{ item }}</span>
                          </div>
                        </v-col>
                      </v-row>

                      <h3 class="text-h6 font-weight-bold mt-5 mb-3">
                        Requirements
                      </h3>
                      <v-list>
                        <v-list-item
                          v-for="(req, index) in course.requirements || []"
                          :key="index"
                        >
                          <v-list-item-icon>
                            <v-icon color="primary">mdi-arrow-right</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title>{{ req }}</v-list-item-title>
                          </v-list-item-content>
                        </v-list-item>
                      </v-list>
                    </div>
                  </v-tab-item>

                  <!-- Curriculum Tab -->
                  <v-tab-item>
                    <div class="py-4">
                      <v-expansion-panels>
                        <v-expansion-panel
                          v-for="(section, sectionIndex) in course.sections ||
                          []"
                          :key="sectionIndex"
                        >
                          <v-expansion-panel-header>
                            <div
                              class="d-flex justify-space-between align-center width-100"
                            >
                              <div>
                                <span class="font-weight-bold"
                                  >Section {{ sectionIndex + 1 }}:
                                  {{ section.title }}</span
                                >
                              </div>
                              <div class="text-caption">
                                {{
                                  section.lessons ? section.lessons.length : 0
                                }}
                                lessons |
                                {{ calculateSectionDuration(section) }}
                              </div>
                            </div>
                          </v-expansion-panel-header>

                          <v-expansion-panel-content>
                            <v-list>
                              <v-list-item
                                v-for="(
                                  lesson, lessonIndex
                                ) in section.lessons || []"
                                :key="lessonIndex"
                                :disabled="!isEnrolled && !lesson.preview"
                                @click="openLesson(lesson)"
                              >
                                <v-list-item-icon>
                                  <v-icon v-if="lesson.type === 'VIDEO'"
                                    >mdi-play-circle</v-icon
                                  >
                                  <v-icon v-else-if="lesson.type === 'QUIZ'"
                                    >mdi-help-circle</v-icon
                                  >
                                  <v-icon v-else>mdi-file-document</v-icon>
                                </v-list-item-icon>

                                <v-list-item-content>
                                  <v-list-item-title
                                    class="d-flex justify-space-between"
                                  >
                                    <span>{{ lesson.title }}</span>
                                    <span class="text-caption">{{
                                      lesson.duration || "10:00"
                                    }}</span>
                                  </v-list-item-title>
                                </v-list-item-content>

                                <v-list-item-action>
                                  <v-chip
                                    x-small
                                    v-if="lesson.preview"
                                    color="green"
                                    text-color="white"
                                    >Preview</v-chip
                                  >
                                  <v-icon
                                    v-else-if="isLessonCompleted(lesson)"
                                    color="success"
                                    >mdi-check-circle</v-icon
                                  >
                                </v-list-item-action>
                              </v-list-item>
                            </v-list>
                          </v-expansion-panel-content>
                        </v-expansion-panel>
                      </v-expansion-panels>
                    </div>
                  </v-tab-item>

                  <!-- Materials Tab -->
                  <v-tab-item>
                    <div class="py-4">
                      <div
                        v-if="course.materials && course.materials.length > 0"
                      >
                        <v-list>
                          <v-list-item
                            v-for="(material, index) in course.materials"
                            :key="index"
                            :disabled="!isEnrolled"
                            @click="downloadMaterial(material)"
                          >
                            <v-list-item-icon>
                              <v-icon v-if="material.type === 'PDF'"
                                >mdi-file-pdf-box</v-icon
                              >
                              <v-icon v-else-if="material.type === 'DOC'"
                                >mdi-file-word</v-icon
                              >
                              <v-icon v-else-if="material.type === 'PPT'"
                                >mdi-file-powerpoint</v-icon
                              >
                              <v-icon v-else>mdi-file</v-icon>
                            </v-list-item-icon>

                            <v-list-item-content>
                              <v-list-item-title>{{
                                material.title
                              }}</v-list-item-title>
                              <v-list-item-subtitle>{{
                                formatFileSize(material.size)
                              }}</v-list-item-subtitle>
                            </v-list-item-content>

                            <v-list-item-action>
                              <v-btn icon>
                                <v-icon>mdi-download</v-icon>
                              </v-btn>
                            </v-list-item-action>
                          </v-list-item>
                        </v-list>
                      </div>
                      <div v-else class="text-center py-5">
                        <v-icon large color="grey lighten-1"
                          >mdi-file-document-outline</v-icon
                        >
                        <div
                          class="text-subtitle-1 grey--text text--darken-1 mt-3"
                        >
                          No materials available for this course yet.
                        </div>
                      </div>
                    </div>
                  </v-tab-item>

                  <!-- Reviews Tab -->
                  <v-tab-item>
                    <div class="py-4">
                      <div class="d-flex mb-5">
                        <div class="text-center mr-6">
                          <div class="text-h3 font-weight-bold">
                            {{ course.rating || 4.7 }}/5
                          </div>
                          <v-rating
                            :value="course.rating || 4.7"
                            color="amber"
                            dense
                            half-increments
                            readonly
                            size="24"
                          ></v-rating>
                          <div class="text-caption mt-1">
                            {{ course.ratingCount || 42 }} ratings
                          </div>
                        </div>

                        <div class="flex-grow-1">
                          <div
                            v-for="n in 5"
                            :key="n"
                            class="d-flex align-center mb-1"
                          >
                            <div class="mr-2 text-caption">
                              {{ 6 - n }} stars
                            </div>
                            <v-progress-linear
                              :value="getRatingPercentage(6 - n)"
                              height="8"
                              color="amber"
                              class="flex-grow-1"
                            ></v-progress-linear>
                            <div class="ml-2 text-caption">
                              {{ getRatingCount(6 - n) }}
                            </div>
                          </div>
                        </div>
                      </div>

                      <v-divider></v-divider>

                      <div v-if="isEnrolled" class="my-4">
                        <h3 class="text-h6 font-weight-bold mb-3">
                          Write a Review
                        </h3>
                        <v-form @submit.prevent="submitReview">
                          <div class="mb-3">
                            <label class="text-body-2 mb-1 d-block"
                              >Your Rating</label
                            >
                            <v-rating
                              v-model="newReview.rating"
                              color="amber"
                              hover
                              size="32"
                            ></v-rating>
                          </div>

                          <v-textarea
                            v-model="newReview.comment"
                            label="Your Review"
                            outlined
                            rows="4"
                          ></v-textarea>

                          <v-checkbox
                            v-model="newReview.anonymous"
                            label="Post anonymously"
                          ></v-checkbox>

                          <v-btn
                            color="primary"
                            type="submit"
                            :loading="submittingReview"
                          >
                            Submit Review
                          </v-btn>
                        </v-form>
                      </div>

                      <h3 class="text-h6 font-weight-bold my-4">
                        Student Reviews
                      </h3>

                      <div v-if="course.reviews && course.reviews.length > 0">
                        <v-card
                          outlined
                          v-for="(review, index) in course.reviews"
                          :key="index"
                          class="mb-4"
                        >
                          <v-card-text>
                            <div class="d-flex align-center mb-3">
                              <v-avatar
                                size="32"
                                class="mr-3"
                                v-if="!review.anonymous"
                              >
                                <v-img
                                  :src="
                                    review.studentAvatar ||
                                    'https://via.placeholder.com/50'
                                  "
                                ></v-img>
                              </v-avatar>
                              <v-avatar size="32" class="mr-3" v-else>
                                <v-icon>mdi-account</v-icon>
                              </v-avatar>

                              <div>
                                <div class="font-weight-medium">
                                  {{
                                    review.anonymous
                                      ? "Anonymous Student"
                                      : review.studentName
                                  }}
                                </div>
                                <div class="d-flex align-center">
                                  <v-rating
                                    :value="review.rating"
                                    color="amber"
                                    dense
                                    readonly
                                    size="14"
                                  ></v-rating>
                                  <span class="text-caption ml-2">
                                    {{ formatDate(review.createdAt) }}
                                  </span>
                                </div>
                              </div>
                            </div>

                            <div>{{ review.comment }}</div>
                          </v-card-text>
                        </v-card>
                      </div>
                      <div v-else class="text-center py-5">
                        <v-icon large color="grey lighten-1"
                          >mdi-comment-outline</v-icon
                        >
                        <div
                          class="text-subtitle-1 grey--text text--darken-1 mt-3"
                        >
                          No reviews yet. Be the first to review this course!
                        </div>
                      </div>
                    </div>
                  </v-tab-item>

                  <!-- Announcements Tab -->
                  <v-tab-item>
                    <div class="py-4">
                      <div
                        v-if="
                          course.announcements &&
                          course.announcements.length > 0
                        "
                      >
                        <v-timeline>
                          <v-timeline-item
                            v-for="(
                              announcement, index
                            ) in course.announcements"
                            :key="index"
                            color="primary"
                            small
                          >
                            <div class="font-weight-bold">
                              {{ announcement.title }}
                            </div>
                            <div class="text-caption mb-2">
                              {{ formatDate(announcement.createdAt) }}
                            </div>
                            <div>{{ announcement.content }}</div>
                          </v-timeline-item>
                        </v-timeline>
                      </div>
                      <div v-else class="text-center py-5">
                        <v-icon large color="grey lighten-1"
                          >mdi-bullhorn-outline</v-icon
                        >
                        <div
                          class="text-subtitle-1 grey--text text--darken-1 mt-3"
                        >
                          No announcements for this course yet.
                        </div>
                      </div>
                    </div>
                  </v-tab-item>
                </v-tabs-items>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" md="4">
            <v-card class="sticky-card">
              <v-card-text>
                <div v-if="isEnrolled" class="text-center pa-4">
                  <v-icon large color="success" class="mb-2"
                    >mdi-check-circle</v-icon
                  >
                  <div class="text-h6 font-weight-bold">
                    You're enrolled in this course
                  </div>
                  <v-btn
                    color="primary"
                    class="mt-4"
                    block
                    @click="continueLearning"
                  >
                    Continue Learning
                  </v-btn>
                </div>

                <div v-else>
                  <div class="text-center mb-4">
                    <div
                      v-if="course.price > 0"
                      class="text-h4 font-weight-bold"
                    >
                      ${{ course.price.toFixed(2) }}
                    </div>
                    <div v-else class="text-h4 font-weight-bold green--text">
                      Free
                    </div>
                  </div>

                  <v-btn
                    color="primary"
                    block
                    x-large
                    @click="enrollCourse"
                    :loading="enrolling"
                  >
                    {{ course.price > 0 ? "Buy Now" : "Enroll for Free" }}
                  </v-btn>

                  <div class="text-center mt-4 text-caption">
                    <v-icon small>mdi-lock</v-icon>
                    Secure checkout
                  </div>
                </div>
              </v-card-text>

              <v-divider></v-divider>

              <v-list dense>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-play-circle</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >{{ getTotalLessons() }} lessons</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-clock-outline</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >{{ calculateCourseDuration() }} total
                      length</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-account-group</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title>
                      {{
                        course.enrolledStudents
                          ? course.enrolledStudents.length
                          : 0
                      }}
                      students enrolled
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-update</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >Last updated
                      {{ formatDate(course.updatedAt) }}</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-translate</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title>{{
                      course.language || "English"
                    }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon>mdi-certificate</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >Certificate of completion</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>
              </v-list>

              <v-card-actions>
                <v-btn text block @click="openChatRoom">
                  <v-icon left>mdi-chat</v-icon>
                  Course Discussion
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>

      <v-container v-else>
        <v-alert type="error">Course not found</v-alert>
      </v-container>
    </div>

    <!-- Video Dialog -->
    <v-dialog v-model="videoDialog" width="800" persistent>
      <v-card>
        <v-card-title class="headline">
          {{ selectedLesson ? selectedLesson.title : "" }}
          <v-spacer></v-spacer>
          <v-btn icon @click="videoDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text v-if="selectedLesson">
          <VideoPlayer
            v-if="selectedLesson.type === 'VIDEO'"
            :src="selectedLesson.videoUrl"
            :poster="selectedLesson.thumbnail"
            :lesson-id="selectedLesson.lessonId"
            :course-id="course.courseId"
            @completed="onLessonCompleted"
          ></VideoPlayer>

          <div v-else-if="selectedLesson.type === 'QUIZ'">
            <!-- Quiz component would go here -->
            <p>Quiz content will be displayed here.</p>
          </div>

          <div v-else>
            <!-- Document viewer would go here -->
            <p>Document content will be displayed here.</p>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import VideoPlayer from "@/components/course/VideoPlayer.vue";
import moment from "moment";

export default {
  components: {
    VideoPlayer,
  },

  props: {
    id: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      loading: true,
      course: null,
      activeTab: 0,
      enrolling: false,
      videoDialog: false,
      selectedLesson: null,
      completedLessons: [],
      submittingReview: false,
      newReview: {
        rating: 5,
        comment: "",
        anonymous: false,
      },
    };
  },

  computed: {
    isEnrolled() {
      if (!this.course || !this.$store.getters["user/currentUser"])
        return false;

      const userId = this.$store.getters["user/currentUser"].userId;
      return (
        this.course.enrolledStudents &&
        this.course.enrolledStudents.some(
          (student) => student.userId === userId
        )
      );
    },
  },

  created() {
    this.fetchCourse();
    this.fetchCompletedLessons();
  },

  methods: {
    async fetchCourse() {
      try {
        this.loading = true;
        const response = await this.$axios.get(`/courses/${this.id}`);
        this.course = response.data;
      } catch (error) {
        console.error("Failed to fetch course details:", error);
      } finally {
        this.loading = false;
      }
    },

    async fetchCompletedLessons() {
      if (!this.$store.getters["user/isAuthenticated"]) return;

      try {
        const response = await this.$axios.get(`/courses/${this.id}/progress`);
        this.completedLessons = response.data.completedLessons || [];
      } catch (error) {
        console.error("Failed to fetch progress:", error);
      }
    },

    async enrollCourse() {
      if (!this.$store.getters["user/isAuthenticated"]) {
        this.$router.push(`/login?redirect=/courses/${this.id}`);
        return;
      }

      this.enrolling = true;

      try {
        if (this.course.price > 0) {
          // Create order and redirect to payment
          const response = await this.$axios.post("/orders", {
            courseId: this.course.courseId,
            amount: this.course.price,
          });

          this.$router.push(`/payment/${response.data.orderId}`);
        } else {
          // Free course - enroll directly
          await this.$store.dispatch(
            "course/enrollCourse",
            this.course.courseId
          );
          // Refresh the course data to update enrollment status
          this.fetchCourse();
        }
      } catch (error) {
        console.error("Failed to enroll in course:", error);
      } finally {
        this.enrolling = false;
      }
    },

    continueLearning() {
      // Find the first incomplete lesson and open it
      const allLessons = this.getAllLessons();
      for (const lesson of allLessons) {
        if (!this.isLessonCompleted(lesson)) {
          this.openLesson(lesson);
          return;
        }
      }

      // If all lessons completed, open the first lesson
      if (allLessons.length > 0) {
        this.openLesson(allLessons[0]);
      }
    },

    openLesson(lesson) {
      if (!this.isEnrolled && !lesson.preview) {
        // If not enrolled and not a preview lesson, show enrollment message
        this.$vuetify.dialog
          .confirm({
            title: "Enroll in this course",
            text: "You need to enroll in this course to access this lesson.",
            cancelText: "Cancel",
            confirmText: "Enroll Now",
            persistent: true,
          })
          .then((result) => {
            if (result) {
              this.enrollCourse();
            }
          });
        return;
      }

      this.selectedLesson = lesson;
      this.videoDialog = true;
    },

    onLessonCompleted() {
      if (this.selectedLesson) {
        // Add to completed lessons if not already there
        if (!this.completedLessons.includes(this.selectedLesson.lessonId)) {
          this.completedLessons.push(this.selectedLesson.lessonId);
        }
      }
    },

    isLessonCompleted(lesson) {
      return this.completedLessons.includes(lesson.lessonId);
    },

    async submitReview() {
      if (!this.newReview.rating || !this.newReview.comment) {
        this.$vuetify.dialog.notify({
          text: "Please provide both a rating and comment",
          color: "error",
        });
        return;
      }

      this.submittingReview = true;

      try {
        await this.$axios.post(
          `/courses/${this.course.courseId}/reviews`,
          this.newReview
        );

        // Reset form
        this.newReview = {
          rating: 5,
          comment: "",
          anonymous: false,
        };

        // Refresh course to get updated reviews
        this.fetchCourse();

        this.$vuetify.dialog.notify({
          text: "Your review has been submitted successfully",
          color: "success",
        });
      } catch (error) {
        console.error("Failed to submit review:", error);
        this.$vuetify.dialog.notify({
          text: "Failed to submit review",
          color: "error",
        });
      } finally {
        this.submittingReview = false;
      }
    },

    async openChatRoom() {
      if (!this.$store.getters["user/isAuthenticated"]) {
        this.$router.push(`/login?redirect=/courses/${this.id}`);
        return;
      }

      try {
        const response = await this.$store.dispatch(
          "chat/fetchChatRoomByCourse",
          this.course.courseId
        );
        this.$router.push(`/chat/${response.roomId}`);
      } catch (error) {
        console.error("Failed to open chat room:", error);
      }
    },

    async downloadMaterial(material) {
      if (!this.isEnrolled) {
        this.$vuetify.dialog
          .confirm({
            title: "Enroll in this course",
            text: "You need to enroll in this course to access course materials.",
            cancelText: "Cancel",
            confirmText: "Enroll Now",
            persistent: true,
          })
          .then((result) => {
            if (result) {
              this.enrollCourse();
            }
          });
        return;
      }

      try {
        window.open(material.fileUrl, "_blank");
      } catch (error) {
        console.error("Failed to download material:", error);
      }
    },

    getAllLessons() {
      if (!this.course || !this.course.sections) return [];

      let allLessons = [];
      this.course.sections.forEach((section) => {
        if (section.lessons) {
          allLessons = allLessons.concat(section.lessons);
        }
      });

      return allLessons;
    },

    getTotalLessons() {
      return this.getAllLessons().length;
    },

    calculateCourseDuration() {
      if (!this.course || !this.course.sections) return "0h 0m";

      let totalMinutes = 0;
      this.course.sections.forEach((section) => {
        if (section.lessons) {
          section.lessons.forEach((lesson) => {
            if (lesson.durationMinutes) {
              totalMinutes += lesson.durationMinutes;
            }
          });
        }
      });

      const hours = Math.floor(totalMinutes / 60);
      const minutes = totalMinutes % 60;

      return `${hours}h ${minutes}m`;
    },

    calculateSectionDuration(section) {
      if (!section || !section.lessons) return "0h 0m";

      let totalMinutes = 0;
      section.lessons.forEach((lesson) => {
        if (lesson.durationMinutes) {
          totalMinutes += lesson.durationMinutes;
        }
      });

      const hours = Math.floor(totalMinutes / 60);
      const minutes = totalMinutes % 60;

      return `${hours}h ${minutes}m`;
    },

    formatDate(date) {
      return moment(date).format("MMM DD, YYYY");
    },

    formatFileSize(bytes) {
      if (!bytes) return "0 Bytes";

      const k = 1024;
      const sizes = ["Bytes", "KB", "MB", "GB", "TB"];
      const i = Math.floor(Math.log(bytes) / Math.log(k));

      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
    },

    getRatingPercentage(stars) {
      if (!this.course || !this.course.ratingDistribution) return 0;

      const count = this.getRatingCount(stars);
      const total = this.course.ratingCount || 0;

      return total > 0 ? (count / total) * 100 : 0;
    },

    getRatingCount(stars) {
      if (!this.course || !this.course.ratingDistribution) return 0;

      return this.course.ratingDistribution[stars] || 0;
    },

    getLevelColor(level) {
      switch (level) {
        case "BEGINNER":
          return "green";
        case "INTERMEDIATE":
          return "blue";
        case "ADVANCED":
          return "purple";
        default:
          return "grey";
      }
    },
  },
};
</script>

<style>
.bg-black-transparent {
  background-color: rgba(0, 0, 0, 0.5);
}

.sticky-card {
  position: sticky;
  top: 80px;
}

.width-100 {
  width: 100%;
}
</style>
