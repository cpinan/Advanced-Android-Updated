package com.carlospinan.androidemojifykotlin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.carlospinan.androidemojifykotlin.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark

private val options = FirebaseVisionFaceDetectorOptions.Builder()
    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
    .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
    .build()

private val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

enum class Emoji {
    SMILE,
    FROWN,
    LEFT_WINK,
    RIGHT_WINK,
    LEFT_WINK_FROWN,
    RIGHT_WINK_FROWN,
    CLOSED_EYE_SMILE,
    CLOSED_EYE_FROWN
}

private const val LOG_TAG = "Emojifier"
private const val SMILING_PROB_THRESHOLD = .15
private const val EYE_OPEN_PROB_THRESHOLD = .5
private const val EMOJI_SCALE_FACTOR = 1.1f

fun log(message: String) {
    Log.d(LOG_TAG, "$message")
}

object Emojifier {

    /**
     * Combines the original picture with the emoji bitmaps
     *
     * @param backgroundBitmap The original picture
     * @param emojiBitmap      The chosen emoji
     * @param face             The detected face
     * @return The final bitmap, including the emojis over the faces
     */
    private fun addBitmapToFace(
        bitmap: Bitmap,
        emojiBitmap: Bitmap,
        face: FirebaseVisionFace
    ): Bitmap {
        // Initialize the results bitmap to be a mutable copy of the original image
        val resultBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            bitmap.config
        )
        val boundingBox = face.boundingBox
        val faceWidth = boundingBox.right - boundingBox.left
        val faceHeight = boundingBox.bottom - boundingBox.top
        val faceX = boundingBox.left
        val faceY = boundingBox.top

        // Scale the emoji so it looks better on the face
        val scaleFactor = EMOJI_SCALE_FACTOR

        // Determine the size of the emoji to match the width of the face and preserve aspect ratio
        val newEmojiWidth = (faceWidth * scaleFactor)
        val newEmojiHeight =
            (emojiBitmap.height * newEmojiWidth / emojiBitmap.width * scaleFactor)


        // Scale the emoji
        val customEmojiBitmap =
            Bitmap.createScaledBitmap(
                emojiBitmap,
                newEmojiWidth.toInt(),
                newEmojiHeight.toInt(),
                false
            )

        // Determine the emoji position so it best lines up with the face
        val emojiPositionX = faceX + faceWidth / 2 - customEmojiBitmap.width / 2
        val emojiPositionY = faceY + faceHeight / 2 - customEmojiBitmap.height / 2

        // Create the canvas and draw the bitmaps to it
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
        canvas.drawBitmap(
            customEmojiBitmap,
            emojiPositionX.toFloat(),
            emojiPositionY.toFloat(),
            null
        )
        return resultBitmap
    }

    private fun getEmojiBitmap(emoji: Emoji, context: Context): Bitmap {
        return when (emoji) {
            Emoji.SMILE -> BitmapFactory.decodeResource(context.resources, R.drawable.smile)
            Emoji.FROWN -> BitmapFactory.decodeResource(context.resources, R.drawable.frown)
            Emoji.LEFT_WINK -> BitmapFactory.decodeResource(context.resources, R.drawable.leftwink)
            Emoji.RIGHT_WINK -> BitmapFactory.decodeResource(
                context.resources,
                R.drawable.rightwink
            )
            Emoji.LEFT_WINK_FROWN -> BitmapFactory.decodeResource(
                context.resources,
                R.drawable.leftwinkfrown
            )
            Emoji.RIGHT_WINK_FROWN -> BitmapFactory.decodeResource(
                context.resources,
                R.drawable.rightwinkfrown
            )
            Emoji.CLOSED_EYE_SMILE -> BitmapFactory.decodeResource(
                context.resources,
                R.drawable.closed_smile
            )
            Emoji.CLOSED_EYE_FROWN -> BitmapFactory.decodeResource(
                context.resources,
                R.drawable.closed_frown
            )
        }
    }

    private fun whichEmoji(face: FirebaseVisionFace): Emoji {
        // Log all the probabilities
        log("whichEmoji: smilingProb = ${face.smilingProbability}")
        log("whichEmoji: leftEyeOpenProb = ${face.leftEyeOpenProbability}")
        log("whichEmoji: rightEyeOpenProb = ${face.rightEyeOpenProbability}")

        val isSmiling = face.smilingProbability > SMILING_PROB_THRESHOLD
        val isLeftEyeClosed = face.leftEyeOpenProbability < EYE_OPEN_PROB_THRESHOLD
        val isRightEyeClosed = face.rightEyeOpenProbability < EYE_OPEN_PROB_THRESHOLD

        var emoji: Emoji =
            if (isSmiling) {
                if (isLeftEyeClosed && !isRightEyeClosed) {
                    Emoji.LEFT_WINK
                } else if (isRightEyeClosed && !isLeftEyeClosed) {
                    Emoji.RIGHT_WINK
                } else if (isLeftEyeClosed) {
                    Emoji.CLOSED_EYE_SMILE
                } else {
                    Emoji.SMILE
                }
            } else {
                if (isLeftEyeClosed && !isRightEyeClosed) {
                    Emoji.LEFT_WINK_FROWN
                } else if (isRightEyeClosed && !isLeftEyeClosed) {
                    Emoji.RIGHT_WINK_FROWN
                } else if (isLeftEyeClosed) {
                    Emoji.CLOSED_EYE_FROWN
                } else {
                    Emoji.FROWN
                }
            }
        log("whichEmoji: ${emoji.name}")
        return emoji
    }

    fun detectFaces(context: Context, bitmap: Bitmap, bitmapLiveData: MutableLiveData<Bitmap>) {
        var editedBitmap = bitmap
        val visionImage = FirebaseVisionImage.fromBitmap(editedBitmap)
        var facesDetected = false
        detector.detectInImage(visionImage)
            .addOnCompleteListener {
                if (!facesDetected) {
                    log("addOnCompleteListener")
                    bitmapLiveData.postValue(editedBitmap)
                }
            }
            .addOnSuccessListener { faces ->
                facesDetected = true
                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                    // nose available):
                    val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
                    leftEar?.let {
                        val leftEarPos = leftEar.position
                        log("$leftEarPos")
                    }

                    // If classification was enabled:
                    if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        val smileProb = face.smilingProbability
                        log("$smileProb")
                    }
                    if (face.rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                        log("$rightEyeOpenProb")
                    }

                    // If face tracking was enabled:
                    if (face.trackingId != FirebaseVisionFace.INVALID_ID) {
                        val id = face.trackingId
                        log("$id")
                    }

                    log("$bounds")
                    log("$rotY")
                    log("$rotZ")
                    val emoji = whichEmoji(face)
                    val emojiBitmap = getEmojiBitmap(emoji, context)
                    editedBitmap = addBitmapToFace(editedBitmap, emojiBitmap, face)
                }
                bitmapLiveData.postValue(editedBitmap)
            }
            .addOnFailureListener { exception ->
                throw exception
            }
    }

}