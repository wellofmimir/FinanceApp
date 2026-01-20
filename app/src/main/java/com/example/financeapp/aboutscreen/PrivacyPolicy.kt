package com.example.financeapp.aboutscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun PrivacyPolicy (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val verticalScrollState = rememberScrollState()

    Box (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text (
            text = buildAnnotatedString {
                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Privacy Policy\n\n")
                }

                append("This privacy policy applies to the Greeen app (hereby referred to as \"Application\") for mobile devices that was created by Patryk Mleczko & Erin MacGregor (hereby referred to as \"Service Provider\") as an Ad Supported service. This service is intended for use \"AS IS\".\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Information Collection and Use\n\n")
                }

                append("The Application collects information when you download and use it. This information may include information such as\n" +
                        "\n" +
                        "Your device's Internet Protocol address (e.g. IP address)\n" +
                        "The pages of the Application that you visit, the time and date of your visit, the time spent on those pages\n" +
                        "The time spent on the Application\n" +
                        "The operating system you use on your mobile device\n" +
                        "\n" +
                        "The Application does not gather precise information about the location of your mobile device.\n" +
                        "\n" +
                        "\n" +
                        "The Service Provider may use the information you provided to contact you from time to time to provide you with important information, required notices and marketing promotions.\n" +
                        "\n" +
                        "\n" +
                        "For a better experience, while using the Application, the Service Provider may require you to provide us with certain personally identifiable information. The information that the Service Provider request will be retained by them and used as described in this privacy policy.\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Third Party Access\n\n")
                }

                append("Only aggregated, anonymized data is periodically transmitted to external services to aid the Service Provider in improving the Application and their service. The Service Provider may share your information with third parties in the ways that are described in this privacy statement.\n" +
                        "\n" +
                        "\n" +
                        "The Service Provider may disclose User Provided and Automatically Collected Information:\n" +
                        "\n" +
                        "as required by law, such as to comply with a subpoena, or similar legal process;\n" +
                        "when they believe in good faith that disclosure is necessary to protect their rights, protect your safety or the safety of others, investigate fraud, or respond to a government request;\n" +
                        "with their trusted services providers who work on their behalf, do not have an independent use of the information we disclose to them, and have agreed to adhere to the rules set forth in this privacy statement.\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Opt-Out Rights\n\n")
                }

                append("You can stop all collection of information by the Application easily by uninstalling it. You may use the standard uninstall processes as may be available as part of your mobile device or via the mobile application marketplace or network.\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Data Retention Policy\n\n")
                }

                append("The Service Provider will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. If you'd like them to delete User Provided Data that you have provided via the Application, please contact them at greeen.development.team@gmail.com and they will respond in a reasonable time.\n" + "\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Children\n\n")
                }

                append("The Service Provider does not use the Application to knowingly solicit data from or market to children under the age of 13.\n" +
                        "\n" +
                        "\n" +
                        "The Service Provider does not knowingly collect personally identifiable information from children. The Service Provider encourages all children to never submit any personally identifiable information through the Application and/or Services. The Service Provider encourage parents and legal guardians to monitor their children's Internet usage and to help enforce this Policy by instructing their children never to provide personally identifiable information through the Application and/or Services without their permission. If you have reason to believe that a child has provided personally identifiable information to the Service Provider through the Application and/or Services, please contact the Service Provider (greeen.development.team@gmail.com) so that they will be able to take the necessary actions. You must also be at least 16 years of age to consent to the processing of your personally identifiable information in your country (in some countries we may allow your parent or guardian to do so on your behalf).\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Security\n\n")
                }

                append("The Service Provider is concerned about safeguarding the confidentiality of your information. The Service Provider provides physical, electronic, and procedural safeguards to protect information the Service Provider processes and maintains.\n" + "\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Changes\n\n")
                }

                append("This Privacy Policy may be updated from time to time for any reason. The Service Provider will notify you of any changes to the Privacy Policy by updating this page with the new Privacy Policy. You are advised to consult this Privacy Policy regularly for any changes, as continued use is deemed approval of all changes.\n" +
                        "\n" +
                        "\n" +
                        "This privacy policy is effective as of 2026-01-16\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Your Consent\n\n")
                }

                append("By using the Application, you are consenting to the processing of your information as set forth in this Privacy Policy now and as amended by us.\n\n")

                withStyle (
                    style = SpanStyle (
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append("Contact Us\n\n")
                }

                append("If you have any questions regarding privacy while using the Application, or have questions about the practices, please contact the Service Provider via email at greeen.development.team@gmail.com.")
            },
            color = colors.primary,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .verticalScroll(verticalScrollState),
            )
    }
}