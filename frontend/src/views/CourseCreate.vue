<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="12">
          <h1 class="text-h4 font-weight-bold">Create New Course</h1>
          <v-breadcrumbs :items="breadcrumbs" class="pa-0 mb-4"></v-breadcrumbs>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12">
          <v-stepper v-model="currentStep">
            <v-stepper-header>
              <v-stepper-step :complete="currentStep > 1" step="1">
                Basic Information
              </v-stepper-step>

              <v-divider></v-divider>

              <v-stepper-step :complete="currentStep > 2" step="2">
                Course Content
              </v-stepper-step>

              <v-divider></v-divider>

              <v-stepper-step :complete="currentStep > 3" step="3">
                Pricing & Settings
              </v-stepper-step>

              <v-divider></v-divider>

              <v-stepper-step step="4">Review & Submit</v-stepper-step>
            </v-stepper-header>

            <v-stepper-items>
              <!-- Step 1: Basic Information -->
              <v-stepper-content step="1">
                <v-card flat>
                  <v-form ref="basicInfoForm" v-model="forms.basicInfo.valid">
                    <v-card-text>
                      <v-row>
                        <v-col cols="12" md="8">
                          <v-text-field
                            v-model="course.title"
                            label="Course Title"
                            :rules="rules.required"
                            required
                          ></v-text-field>

                          <v-textarea
                            v-model="course.description"
                            label="Course Description"
                            :rules="rules.required"
                            counter
                            rows="5"
                            required
                          ></v-textarea>

                          <v-row>
                            <v-col cols="12" md="6">
                              <v-select
                                v-model="course.category"
                                :items="categories"
                                label="Category"
                                :rules="rules.required"
                                required
                              ></v-select>
                            </v-col>

                            <v-col cols="12" md="6">
                              <v-select
                                v-model="course.level"
                                :items="levels"
                                label="Difficulty Level"
                                :rules="rules.required"
                                required
                              ></v-select>
                            </v-col>
                          </v-row>

                          <v-text-field
                            v-model="course.language"
                            label="Language"
                            :rules="rules.required"
                            required
                          ></v-text-field>

                          <v-combobox
                            v-model="course.tags"
                            label="Tags"
                            hint="Add tags to help students find your course"
                            multiple
                            chips
                            small-chips
                          ></v-combobox>

                          <h3 class="text-h6 mt-5 mb-3">Learning Objectives</h3>
                          <p class="text-subtitle-2 text-grey mb-4">
                            What will students be able to do after taking your
                            course?
                          </p>

                          <div
                            v-for="(
                              objective, index
                            ) in course.learningObjectives"
                            :key="`objective-${index}`"
                            class="d-flex align-center mb-2"
                          >
                            <v-text-field
                              v-model="course.learningObjectives[index]"
                              :label="`Objective ${index + 1}`"
                              hide-details
                              class="mr-2"
                            ></v-text-field>

                            <v-btn icon @click="removeObjective(index)">
                              <v-icon color="error">mdi-delete</v-icon>
                            </v-btn>
                          </div>

                          <v-btn
                            text
                            color="primary"
                            class="mt-2"
                            @click="addObjective"
                          >
                            <v-icon left>mdi-plus</v-icon>
                            Add Learning Objective
                          </v-btn>

                          <h3 class="text-h6 mt-5 mb-3">Prerequisites</h3>
                          <p class="text-subtitle-2 text-grey mb-4">
                            What should students know before starting your
                            course?
                          </p>

                          <div
                            v-for="(requirement, index) in course.requirements"
                            :key="`requirement-${index}`"
                            class="d-flex align-center mb-2"
                          >
                            <v-text-field
                              v-model="course.requirements[index]"
                              :label="`Requirement ${index + 1}`"
                              hide-details
                              class="mr-2"
                            ></v-text-field>

                            <v-btn icon @click="removeRequirement(index)">
                              <v-icon color="error">mdi-delete</v-icon>
                            </v-btn>
                          </div>

                          <v-btn
                            text
                            color="primary"
                            class="mt-2"
                            @click="addRequirement"
                          >
                            <v-icon left>mdi-plus</v-icon>
                            Add Requirement
                          </v-btn>
                        </v-col>

                        <v-col cols="12" md="4">
                          <v-card outlined>
                            <v-card-title>Course Thumbnail</v-card-title>

                            <v-card-text class="text-center">
                              <v-img
                                :src="
                                  thumbnailPreview ||
                                  require('@/assets/upload-placeholder.jpg')
                                "
                                aspect-ratio="16/9"
                                max-height="200"
                                class="mb-4 mx-auto"
                                contain
                              ></v-img>

                              <v-file-input
                                v-model="thumbnailFile"
                                accept="image/*"
                                label="Select Image"
                                prepend-icon="mdi-camera"
                                @change="previewThumbnail"
                                :rules="rules.image"
                                hint="Recommended size: 1280x720 pixels"
                                persistent-hint
                              ></v-file-input>
                            </v-card-text>
                          </v-card>

                          <v-card outlined class="mt-4">
                            <v-card-title>Promotional Video</v-card-title>

                            <v-card-text class="text-center">
                              <div v-if="promoVideoPreview" class="mb-4">
                                <video
                                  ref="promoVideoPreview"
                                  width="100%"
                                  controls
                                >
                                  <source
                                    :src="promoVideoPreview"
                                    type="video/mp4"
                                  />
                                  Your browser does not support the video tag.
                                </video>
                              </div>
                              <div v-else class="promo-placeholder mb-4">
                                <v-icon size="48">mdi-video</v-icon>
                                <div class="text-subtitle-1 mt-2">
                                  No video selected
                                </div>
                              </div>

                              <v-file-input
                                v-model="promoVideoFile"
                                accept="video/*"
                                label="Select Video"
                                prepend-icon="mdi-video"
                                @change="previewPromoVideo"
                                hint="Maximum size: 100MB"
                                persistent-hint
                              ></v-file-input>
                            </v-card-text>
                          </v-card>
                        </v-col>
                      </v-row>
                    </v-card-text>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                        color="primary"
                        @click="validateAndProceed(1)"
                        :disabled="!forms.basicInfo.valid"
                      >
                        Continue
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-actions>
                  </v-form>
                </v-card>
              </v-stepper-content>

              <!-- Step 2: Course Content -->
              <v-stepper-content step="2">
                <v-card flat>
                  <v-card-text>
                    <h3 class="text-h6 mb-3">Course Curriculum</h3>
                    <p class="text-subtitle-2 text-grey mb-4">
                      Organize your course into sections and lessons.
                    </p>

                    <v-expansion-panels v-model="openSection">
                      <v-expansion-panel
                        v-for="(section, sectionIndex) in course.sections"
                        :key="`section-${sectionIndex}`"
                      >
                        <v-expansion-panel-header>
                          <div class="d-flex align-center">
                            <span class="font-weight-medium"
                              >Section {{ sectionIndex + 1 }}:
                              {{ section.title }}</span
                            >
                            <v-spacer></v-spacer>
                            <v-chip small class="mr-2"
                              >{{
                                section.lessons ? section.lessons.length : 0
                              }}
                              lessons</v-chip
                            >
                            <v-btn
                              icon
                              small
                              @click.stop="removeSection(sectionIndex)"
                            >
                              <v-icon color="error" small>mdi-delete</v-icon>
                            </v-btn>
                          </div>
                        </v-expansion-panel-header>

                        <v-expansion-panel-content>
                          <v-text-field
                            v-model="section.title"
                            label="Section Title"
                            :rules="rules.required"
                            required
                          ></v-text-field>

                          <v-text-field
                            v-model="section.description"
                            label="Section Description (optional)"
                          ></v-text-field>

                          <v-divider class="my-4"></v-divider>

                          <h4 class="text-subtitle-1 font-weight-medium mb-3">
                            Lessons
                          </h4>

                          <div
                            v-for="(lesson, lessonIndex) in section.lessons"
                            :key="`lesson-${sectionIndex}-${lessonIndex}`"
                            class="lesson-item pa-3 mb-3"
                          >
                            <div class="d-flex align-center mb-2">
                              <v-icon
                                class="mr-2"
                                :color="getLessonTypeColor(lesson.type)"
                              >
                                {{ getLessonTypeIcon(lesson.type) }}
                              </v-icon>
                              <span class="font-weight-medium">
                                Lesson {{ lessonIndex + 1 }}: {{ lesson.title }}
                              </span>
                              <v-spacer></v-spacer>
                              <v-chip
                                x-small
                                v-if="lesson.preview"
                                class="mr-2"
                                color="green"
                                text-color="white"
                              >
                                Preview
                              </v-chip>
                              <v-btn
                                icon
                                x-small
                                @click="removeLesson(sectionIndex, lessonIndex)"
                              >
                                <v-icon color="error" small>mdi-delete</v-icon>
                              </v-btn>
                            </div>

                            <v-row>
                              <v-col cols="12" md="6">
                                <v-text-field
                                  v-model="lesson.title"
                                  label="Lesson Title"
                                  dense
                                  :rules="rules.required"
                                  required
                                ></v-text-field>
                              </v-col>

                              <v-col cols="12" md="3">
                                <v-select
                                  v-model="lesson.type"
                                  :items="lessonTypes"
                                  label="Type"
                                  dense
                                  :rules="rules.required"
                                  required
                                ></v-select>
                              </v-col>

                              <v-col cols="12" md="3">
                                <v-text-field
                                  v-model="lesson.durationMinutes"
                                  label="Duration (minutes)"
                                  type="number"
                                  dense
                                  min="1"
                                ></v-text-field>
                              </v-col>
                            </v-row>

                            <v-row>
                              <v-col cols="12">
                                <v-textarea
                                  v-model="lesson.description"
                                  label="Lesson Description"
                                  dense
                                  rows="2"
                                ></v-textarea>
                              </v-col>
                            </v-row>

                            <!-- Different fields based on lesson type -->
                            <div v-if="lesson.type === 'VIDEO'">
                              <v-row>
                                <v-col cols="12">
                                  <v-file-input
                                    v-model="lesson.videoFile"
                                    label="Upload Video"
                                    accept="video/*"
                                    prepend-icon="mdi-video"
                                    :rules="
                                      lesson.videoUrl ? [] : rules.required
                                    "
                                    :disabled="!!lesson.videoUrl"
                                  ></v-file-input>
                                </v-col>
                              </v-row>
                              <v-row v-if="lesson.videoUrl">
                                <v-col cols="12">
                                  <div class="d-flex align-center">
                                    <v-icon color="success" class="mr-2"
                                      >mdi-check-circle</v-icon
                                    >
                                    <span>Video already uploaded</span>
                                    <v-spacer></v-spacer>
                                    <v-btn
                                      small
                                      text
                                      color="error"
                                      @click="lesson.videoUrl = null"
                                    >
                                      Replace
                                    </v-btn>
                                  </div>
                                </v-col>
                              </v-row>
                            </div>

                            <div v-else-if="lesson.type === 'QUIZ'">
                              <v-row>
                                <v-col cols="12">
                                  <p class="text-subtitle-2 text-grey">
                                    Quiz questions will be added after course
                                    creation.
                                  </p>
                                </v-col>
                              </v-row>
                            </div>

                            <div v-else-if="lesson.type === 'DOCUMENT'">
                              <v-row>
                                <v-col cols="12">
                                  <v-file-input
                                    v-model="lesson.documentFile"
                                    label="Upload Document"
                                    accept=".pdf,.doc,.docx,.ppt,.pptx"
                                    prepend-icon="mdi-file-document"
                                    :rules="
                                      lesson.documentUrl ? [] : rules.required
                                    "
                                    :disabled="!!lesson.documentUrl"
                                  ></v-file-input>
                                </v-col>
                              </v-row>
                              <v-row v-if="lesson.documentUrl">
                                <v-col cols="12">
                                  <div class="d-flex align-center">
                                    <v-icon color="success" class="mr-2"
                                      >mdi-check-circle</v-icon
                                    >
                                    <span>Document already uploaded</span>
                                    <v-spacer></v-spacer>
                                    <v-btn
                                      small
                                      text
                                      color="error"
                                      @click="lesson.documentUrl = null"
                                    >
                                      Replace
                                    </v-btn>
                                  </div>
                                </v-col>
                              </v-row>
                            </div>

                            <v-row>
                              <v-col cols="12">
                                <v-checkbox
                                  v-model="lesson.preview"
                                  label="Make this lesson available as a preview"
                                  dense
                                ></v-checkbox>
                              </v-col>
                            </v-row>
                          </div>

                          <v-btn
                            color="secondary"
                            text
                            @click="addLesson(sectionIndex)"
                          >
                            <v-icon left>mdi-plus</v-icon>
                            Add Lesson
                          </v-btn>
                        </v-expansion-panel-content>
                      </v-expansion-panel>
                    </v-expansion-panels>

                    <div class="text-center mt-4">
                      <v-btn color="secondary" outlined @click="addSection">
                        <v-icon left>mdi-plus</v-icon>
                        Add Section
                      </v-btn>
                    </div>
                  </v-card-text>

                  <v-divider></v-divider>

                  <v-card-actions>
                    <v-btn text @click="currentStep = 1">
                      <v-icon left>mdi-arrow-left</v-icon>
                      Back
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="primary"
                      @click="validateAndProceed(2)"
                      :disabled="!validateCurriculum()"
                    >
                      Continue
                      <v-icon right>mdi-arrow-right</v-icon>
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-stepper-content>

              <!-- Step 3: Pricing & Settings -->
              <v-stepper-content step="3">
                <v-card flat>
                  <v-form ref="pricingForm" v-model="forms.pricing.valid">
                    <v-card-text>
                      <v-row>
                        <v-col cols="12" md="6">
                          <h3 class="text-h6 mb-3">Course Pricing</h3>

                          <v-radio-group
                            v-model="course.pricingModel"
                            :rules="rules.required"
                            required
                          >
                            <v-radio value="FREE" label="Free Course"></v-radio>

                            <v-radio value="PAID" label="Paid Course"></v-radio>
                          </v-radio-group>

                          <v-text-field
                            v-if="course.pricingModel === 'PAID'"
                            v-model="course.price"
                            label="Price"
                            type="number"
                            prefix="$"
                            :rules="rules.price"
                            required
                          ></v-text-field>

                          <h3 class="text-h6 mt-5 mb-3">Course Access</h3>

                          <v-select
                            v-model="course.accessType"
                            :items="accessTypes"
                            label="Access Type"
                            :rules="rules.required"
                            required
                          ></v-select>

                          <v-text-field
                            v-if="course.accessType === 'TIME_LIMITED'"
                            v-model="course.accessDuration"
                            label="Access Duration (days)"
                            type="number"
                            min="1"
                            :rules="rules.required"
                            required
                          ></v-text-field>
                        </v-col>

                        <v-col cols="12" md="6">
                          <h3 class="text-h6 mb-3">Publication Settings</h3>

                          <v-radio-group
                            v-model="course.status"
                            :rules="rules.required"
                            required
                          >
                            <v-radio
                              value="PUBLISHED"
                              label="Publish immediately after approval"
                            ></v-radio>

                            <v-radio
                              value="DRAFT"
                              label="Save as draft"
                            ></v-radio>
                          </v-radio-group>

                          <v-checkbox
                            v-model="course.certificateEnabled"
                            label="Enable course completion certificate"
                          ></v-checkbox>

                          <v-checkbox
                            v-model="course.featuredRequest"
                            label="Request to be featured on homepage"
                          ></v-checkbox>
                        </v-col>
                      </v-row>
                    </v-card-text>

                    <v-divider></v-divider>

                    <v-card-actions>
                      <v-btn text @click="currentStep = 2">
                        <v-icon left>mdi-arrow-left</v-icon>
                        Back
                      </v-btn>
                      <v-spacer></v-spacer>
                      <v-btn
                        color="primary"
                        @click="validateAndProceed(3)"
                        :disabled="!forms.pricing.valid"
                      >
                        Continue
                        <v-icon right>mdi-arrow-right</v-icon>
                      </v-btn>
                    </v-card-actions>
                  </v-form>
                </v-card>
              </v-stepper-content>

              <!-- Step 4: Review & Submit -->
              <v-stepper-content step="4">
                <v-card flat>
                  <v-card-text>
                    <h3 class="text-h6 mb-4">Review Your Course</h3>

                    <v-row>
                      <v-col cols="12" md="4">
                        <v-img
                          :src="
                            thumbnailPreview ||
                            require('@/assets/course-placeholder.jpg')
                          "
                          aspect-ratio="16/9"
                          class="rounded-lg"
                        ></v-img>
                      </v-col>

                      <v-col cols="12" md="8">
                        <h2 class="text-h5 font-weight-bold">
                          {{ course.title }}
                        </h2>

                        <div class="d-flex align-center my-2">
                          <v-chip small color="primary" class="mr-2">{{
                            course.category
                          }}</v-chip>
                          <v-chip small color="secondary" class="mr-2">{{
                            course.level
                          }}</v-chip>
                          <v-chip
                            small
                            color="grey"
                            v-if="course.pricingModel === 'FREE'"
                            >Free</v-chip
                          >
                          <v-chip small color="green" v-else
                            >${{ course.price }}</v-chip
                          >
                        </div>

                        <v-divider class="my-3"></v-divider>

                        <p class="text-subtitle-1">{{ course.description }}</p>
                      </v-col>
                    </v-row>

                    <v-divider class="my-4"></v-divider>

                    <h3 class="text-h6 mb-3">Course Curriculum</h3>

                    <v-list>
                      <template
                        v-for="(section, sectionIndex) in course.sections"
                      >
                        <v-list-item :key="`review-section-${sectionIndex}`">
                          <v-list-item-content>
                            <v-list-item-title class="font-weight-bold">
                              Section {{ sectionIndex + 1 }}:
                              {{ section.title }}
                            </v-list-item-title>
                            <v-list-item-subtitle v-if="section.description">
                              {{ section.description }}
                            </v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-list-item
                          v-for="(lesson, lessonIndex) in section.lessons"
                          :key="`review-lesson-${sectionIndex}-${lessonIndex}`"
                          class="ml-4"
                          dense
                        >
                          <v-list-item-icon>
                            <v-icon :color="getLessonTypeColor(lesson.type)">
                              {{ getLessonTypeIcon(lesson.type) }}
                            </v-icon>
                          </v-list-item-icon>

                          <v-list-item-content>
                            <v-list-item-title>
                              {{ lesson.title }}
                              <v-chip
                                x-small
                                v-if="lesson.preview"
                                class="ml-2"
                                color="green"
                                text-color="white"
                              >
                                Preview
                              </v-chip>
                            </v-list-item-title>
                            <v-list-item-subtitle v-if="lesson.durationMinutes">
                              {{ lesson.durationMinutes }} minutes
                            </v-list-item-subtitle>
                          </v-list-item-content>
                        </v-list-item>

                        <v-divider
                          :key="`review-divider-${sectionIndex}`"
                          v-if="sectionIndex < course.sections.length - 1"
                        ></v-divider>
                      </template>
                    </v-list>

                    <v-divider class="my-4"></v-divider>

                    <v-row>
                      <v-col cols="12" md="6">
                        <h3 class="text-h6 mb-3">Learning Objectives</h3>
                        <v-list dense>
                          <v-list-item
                            v-for="(
                              objective, index
                            ) in course.learningObjectives"
                            :key="`review-objective-${index}`"
                          >
                            <v-list-item-icon>
                              <v-icon color="success">mdi-check-circle</v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                              <v-list-item-title>{{
                                objective
                              }}</v-list-item-title>
                            </v-list-item-content>
                          </v-list-item>
                        </v-list>
                      </v-col>

                      <v-col cols="12" md="6">
                        <h3 class="text-h6 mb-3">Requirements</h3>
                        <v-list dense>
                          <v-list-item
                            v-for="(requirement, index) in course.requirements"
                            :key="`review-requirement-${index}`"
                          >
                            <v-list-item-icon>
                              <v-icon color="primary"
                                >mdi-arrow-right-circle</v-icon
                              >
                            </v-list-item-icon>
                            <v-list-item-content>
                              <v-list-item-title>{{
                                requirement
                              }}</v-list-item-title>
                            </v-list-item-content>
                          </v-list-item>
                        </v-list>
                      </v-col>
                    </v-row>

                    <v-divider class="my-4"></v-divider>

                    <h3 class="text-h6 mb-3">Additional Information</h3>

                    <v-simple-table>
                      <template v-slot:default>
                        <tbody>
                          <tr>
                            <td>Language</td>
                            <td>{{ course.language }}</td>
                          </tr>
                          <tr>
                            <td>Access Type</td>
                            <td>
                              {{
                                course.accessType === "LIFETIME"
                                  ? "Lifetime Access"
                                  : `Time Limited (${course.accessDuration} days)`
                              }}
                            </td>
                          </tr>
                          <tr>
                            <td>Certificate</td>
                            <td>
                              {{
                                course.certificateEnabled
                                  ? "Enabled"
                                  : "Disabled"
                              }}
                            </td>
                          </tr>
                          <tr>
                            <td>Status</td>
                            <td>
                              {{
                                course.status === "PUBLISHED"
                                  ? "Ready for Review"
                                  : "Draft"
                              }}
                            </td>
                          </tr>
                        </tbody>
                      </template>
                    </v-simple-table>

                    <v-alert type="info" outlined class="mt-4">
                      <p class="mb-0">
                        By submitting this course, you agree to our:
                      </p>
                      <ul class="mb-0">
                        <li>Instructor Terms and Conditions</li>
                        <li>Content Quality Guidelines</li>
                      </ul>
                    </v-alert>
                  </v-card-text>

                  <v-divider></v-divider>

                  <v-card-actions>
                    <v-btn text @click="currentStep = 3">
                      <v-icon left>mdi-arrow-left</v-icon>
                      Back
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn :disabled="loading" text @click="saveDraft">
                      Save as Draft
                    </v-btn>
                    <v-btn
                      color="primary"
                      :loading="loading"
                      @click="submitCourse"
                    >
                      Submit Course
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentStep: 1,
      loading: false,
      openSection: 0,
      thumbnailFile: null,
      thumbnailPreview: null,
      promoVideoFile: null,
      promoVideoPreview: null,

      forms: {
        basicInfo: {
          valid: false,
        },
        pricing: {
          valid: false,
        },
      },

      rules: {
        required: [(v) => !!v || "This field is required"],
        price: [
          (v) => !!v || "Price is required",
          (v) => v > 0 || "Price must be greater than 0",
        ],
        image: [
          (v) =>
            !v || v.size < 2000000 || "Image size should be less than 2 MB!",
        ],
      },

      course: {
        title: "",
        description: "",
        category: "",
        level: "BEGINNER",
        language: "English",
        tags: [],
        learningObjectives: [""],
        requirements: [""],

        sections: [
          {
            title: "Introduction",
            description: "",
            lessons: [
              {
                title: "Welcome to the Course",
                type: "VIDEO",
                description: "",
                durationMinutes: 5,
                preview: true,
                videoFile: null,
                videoUrl: null,
              },
            ],
          },
        ],

        pricingModel: "PAID",
        price: 19.99,
        accessType: "LIFETIME",
        accessDuration: 90,
        status: "PUBLISHED",
        certificateEnabled: true,
        featuredRequest: false,
      },

      categories: [
        "Development",
        "Business",
        "Finance & Accounting",
        "IT & Software",
        "Office Productivity",
        "Personal Development",
        "Design",
        "Marketing",
        "Lifestyle",
        "Photography & Video",
        "Health & Fitness",
        "Music",
        "Teaching & Academics",
      ],

      levels: [
        { text: "Beginner Level", value: "BEGINNER" },
        { text: "Intermediate Level", value: "INTERMEDIATE" },
        { text: "Advanced Level", value: "ADVANCED" },
        { text: "All Levels", value: "ALL_LEVELS" },
      ],

      lessonTypes: [
        { text: "Video", value: "VIDEO" },
        { text: "Quiz", value: "QUIZ" },
        { text: "Document", value: "DOCUMENT" },
      ],

      accessTypes: [
        { text: "Lifetime Access", value: "LIFETIME" },
        { text: "Time Limited Access", value: "TIME_LIMITED" },
      ],

      breadcrumbs: [
        {
          text: "Dashboard",
          disabled: false,
          href: "/teacher/dashboard",
        },
        {
          text: "Create Course",
          disabled: true,
        },
      ],
    };
  },

  methods: {
    validateAndProceed(step) {
      if (step === 1) {
        if (this.$refs.basicInfoForm.validate()) {
          this.currentStep = 2;
        }
      } else if (step === 2) {
        if (this.validateCurriculum()) {
          this.currentStep = 3;
        }
      } else if (step === 3) {
        if (this.$refs.pricingForm.validate()) {
          this.currentStep = 4;
        }
      }
    },

    validateCurriculum() {
      // Check if there is at least one section
      if (this.course.sections.length === 0) {
        return false;
      }

      // Check if all sections have titles and at least one lesson
      for (const section of this.course.sections) {
        if (
          !section.title ||
          !section.lessons ||
          section.lessons.length === 0
        ) {
          return false;
        }

        // Check if all lessons have titles and required fields
        for (const lesson of section.lessons) {
          if (!lesson.title || !lesson.type) {
            return false;
          }

          // Check lesson-specific required fields
          if (
            lesson.type === "VIDEO" &&
            !lesson.videoFile &&
            !lesson.videoUrl
          ) {
            // A video must have either a file or URL
            return false;
          }

          if (
            lesson.type === "DOCUMENT" &&
            !lesson.documentFile &&
            !lesson.documentUrl
          ) {
            // A document must have either a file or URL
            return false;
          }
        }
      }

      return true;
    },

    previewThumbnail(file) {
      if (!file) {
        this.thumbnailPreview = null;
        return;
      }

      this.thumbnailPreview = URL.createObjectURL(file);
    },

    previewPromoVideo(file) {
      if (!file) {
        this.promoVideoPreview = null;
        return;
      }

      this.promoVideoPreview = URL.createObjectURL(file);
    },

    addObjective() {
      this.course.learningObjectives.push("");
    },

    removeObjective(index) {
      this.course.learningObjectives.splice(index, 1);
      if (this.course.learningObjectives.length === 0) {
        this.course.learningObjectives = [""];
      }
    },

    addRequirement() {
      this.course.requirements.push("");
    },

    removeRequirement(index) {
      this.course.requirements.splice(index, 1);
      if (this.course.requirements.length === 0) {
        this.course.requirements = [""];
      }
    },

    addSection() {
      this.course.sections.push({
        title: `Section ${this.course.sections.length + 1}`,
        description: "",
        lessons: [],
      });

      // Open the newly added section
      this.openSection = this.course.sections.length - 1;
    },

    removeSection(index) {
      this.course.sections.splice(index, 1);
    },

    addLesson(sectionIndex) {
      const section = this.course.sections[sectionIndex];
      section.lessons.push({
        title: `Lesson ${section.lessons.length + 1}`,
        type: "VIDEO",
        description: "",
        durationMinutes: 5,
        preview: false,
        videoFile: null,
        videoUrl: null,
      });
    },

    removeLesson(sectionIndex, lessonIndex) {
      this.course.sections[sectionIndex].lessons.splice(lessonIndex, 1);
    },

    getLessonTypeIcon(type) {
      switch (type) {
        case "VIDEO":
          return "mdi-play-circle-outline";
        case "QUIZ":
          return "mdi-help-circle-outline";
        case "DOCUMENT":
          return "mdi-file-document-outline";
        default:
          return "mdi-book-open-variant";
      }
    },

    getLessonTypeColor(type) {
      switch (type) {
        case "VIDEO":
          return "primary";
        case "QUIZ":
          return "warning";
        case "DOCUMENT":
          return "success";
        default:
          return "grey";
      }
    },

    async saveDraft() {
      try {
        this.loading = true;
        this.course.status = "DRAFT";
        await this.createCourse();

        this.$router.push("/teacher/dashboard");
        this.$notify("Course saved as draft", { color: "success" });
      } catch (error) {
        console.error("Failed to save draft:", error);
        this.$notify("Failed to save draft", { color: "error" });
      } finally {
        this.loading = false;
      }
    },

    async submitCourse() {
      try {
        this.loading = true;
        this.course.status = "PUBLISHED";
        await this.createCourse();

        this.$router.push("/teacher/dashboard");
        this.$notify("Course submitted successfully and is pending review", {
          color: "success",
        });
      } catch (error) {
        console.error("Failed to submit course:", error);
        this.$notify("Failed to submit course", { color: "error" });
      } finally {
        this.loading = false;
      }
    },

    async createCourse() {
      // First, create the course to get its ID
      const courseData = {
        title: this.course.title,
        description: this.course.description,
        category: this.course.category,
        level: this.course.level,
        language: this.course.language,
        tags: this.course.tags,
        learningObjectives: this.course.learningObjectives.filter(
          (obj) => obj.trim() !== ""
        ),
        requirements: this.course.requirements.filter(
          (req) => req.trim() !== ""
        ),
        price: this.course.pricingModel === "PAID" ? this.course.price : 0,
        accessType: this.course.accessType,
        accessDuration:
          this.course.accessType === "TIME_LIMITED"
            ? this.course.accessDuration
            : null,
        certificateEnabled: this.course.certificateEnabled,
        featuredRequest: this.course.featuredRequest,
        status: this.course.status,
      };

      const response = await this.$axios.post("/courses", courseData);
      const courseId = response.data.courseId;

      // Upload thumbnail if provided
      if (this.thumbnailFile) {
        const formData = new FormData();
        formData.append("thumbnail", this.thumbnailFile);
        await this.$axios.post(`/courses/${courseId}/thumbnail`, formData);
      }

      // Upload promotional video if provided
      if (this.promoVideoFile) {
        const formData = new FormData();
        formData.append("promo_video", this.promoVideoFile);
        await this.$axios.post(`/courses/${courseId}/promo-video`, formData);
      }

      // Create sections and lessons
      for (const section of this.course.sections) {
        const sectionData = {
          title: section.title,
          description: section.description,
        };

        const sectionResponse = await this.$axios.post(
          `/courses/${courseId}/sections`,
          sectionData
        );
        const sectionId = sectionResponse.data.sectionId;

        // Create lessons for this section
        for (const lesson of section.lessons) {
          const lessonData = {
            title: lesson.title,
            type: lesson.type,
            description: lesson.description,
            durationMinutes: lesson.durationMinutes,
            preview: lesson.preview,
          };

          const lessonResponse = await this.$axios.post(
            `/courses/${courseId}/sections/${sectionId}/lessons`,
            lessonData
          );
          const lessonId = lessonResponse.data.lessonId;

          // Upload lesson content based on type
          if (lesson.type === "VIDEO" && lesson.videoFile) {
            const formData = new FormData();
            formData.append("video", lesson.videoFile);
            await this.$axios.post(`/lessons/${lessonId}/content`, formData);
          } else if (lesson.type === "DOCUMENT" && lesson.documentFile) {
            const formData = new FormData();
            formData.append("document", lesson.documentFile);
            await this.$axios.post(`/lessons/${lessonId}/content`, formData);
          }
        }
      }

      return response.data;
    },
  },
};
</script>

<style scoped>
.lesson-item {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.promo-placeholder {
  height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>
