<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="cards-by-thema" match="OldCard" use="Thema"/>

    <xsl:template match="/OldCards">
        <GroupedCards>
            <xsl:for-each select="OldCard[generate-id() = generate-id(key('cards-by-thema', Thema)[1])]">
                <xsl:sort select="Thema"/>
                <Thema name="{Thema}" count="{count(key('cards-by-thema', Thema))}">
                    <xsl:apply-templates select="key('cards-by-thema', Thema)">
                        <xsl:sort select="Year" data-type="number"/>
                    </xsl:apply-templates>
                </Thema>
            </xsl:for-each>
        </GroupedCards>
    </xsl:template>

    <xsl:template match="OldCard">
        <OldCard>
            <xsl:copy-of select="@id"/>
            <xsl:apply-templates select="*[not(self::Thema)]" mode="clean"/>
        </OldCard>
    </xsl:template>

    <xsl:template match="*" mode="clean">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="clean"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
